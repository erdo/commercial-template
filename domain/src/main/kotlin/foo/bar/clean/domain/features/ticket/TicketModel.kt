package foo.bar.clean.domain.features.ticket

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either
import co.early.fore.kt.core.type.carryOn
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.services.api.Fruit
import foo.bar.clean.domain.services.api.FruitService
import foo.bar.clean.domain.services.api.TicketService
import foo.bar.clean.domain.features.ReadableState
import kotlinx.coroutines.delay

/**
 * This is a basic observable model, the state is not persisted and process death will wipe
 * the state clean, see [CounterModel] for a basic model that persists its state
 *
 * The purpose of this model is to make multiple sequential network calls in order to
 * process a "ticket", if the processing is successful (i.e. none of the networks calls
 * fail) the user is able to claim a free "fruit"
 *
 * Each service connection returns an Either<Error, Data> the carryOn() function only continues if
 * the previous connection was a success. If the previous connection failed, then processing stops
 * and the Either<Error> is passed back up to the result.
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class TicketModel(
    private val fruitService: FruitService,
    private val ticketService: TicketService,
) : ReadableState<TicketState>, Observable by ObservableImp() {

    override var state = TicketState()
        private set

    fun processTicket() {

        Fore.i("processTicket() t:" + Thread.currentThread())

        if (state.loading) {
            return
        }

        state = TicketState(loading = true)
        notifyObservers()

        launchMain {

            startStep(Step.Initialising)

            startStep(Step.CreatingUser)
            val response = ticketService.createUser().carryOn {
                startStep(Step.CreatingTicket)
                ticketService.createTicket(it)
            }.carryOn {
                startStep(Step.FetchingWaitingTime)
                ticketService.getEstimatedWaitingTime(it)
                    .mapSuccess { time -> time to it }
            }.carryOn {
                state = state.copy(waitTimeMin = it.first.minutesWait)
                notifyObservers()
                if (it.first.minutesWait > state.maxAcceptableWaitTimeMin) {
                    startStep(Step.CancellingTicket)
                    ticketService.cancelTicket(it.second)
                } else {
                    startStep(Step.ConfirmingTicket)
                    ticketService.confirmTicket(it.second)
                }
            }.carryOn {
                if (it.completed) {
                    startStep(Step.ClaimingFreeFruit)
                    fruitService.claimFreeFruit(it.ticketRef)
                } else {
                    Either.success(Fruit.FruitNone)
                }
            }

            when (response) {
                is Either.Fail -> {
                    Fore.i("Fail :/ error:${response.value} t:" + Thread.currentThread())
                    failCurrentStep()
                    state = state.copy(
                        freeFruit = Fruit.FruitNone,
                        error = response.value,
                        loading = false
                    )
                    notifyObservers()
                }

                is Either.Success -> {
                    Fore.i("Success! freeFruit:${response.value} t:" + Thread.currentThread())
                    startStep(Step.Complete)
                    state = state.copy(
                        freeFruit = response.value,
                        error = DomainError.NoError,
                        loading = false
                    )
                    notifyObservers()
                }
            }
        }
    }

    fun clearError() {

        Fore.i("clearError() t:" + Thread.currentThread())

        if (state.loading) {
            return
        }

        state = state.copy(
            error = DomainError.NoError,
        )
        notifyObservers()
    }

    fun clearData() {

        Fore.i("clearData() t:" + Thread.currentThread())

        if (state.loading) {
            return
        }

        state = TicketState()
        notifyObservers()
    }

    private suspend fun startStep(step: Step){

        Fore.i("...step:$step t:" + Thread.currentThread())

        if (SLOMO){
            delay(1000)
        }

        state = state.copy(
            progress = state.progress.map{ stepAndStatus ->
                if (stepAndStatus.second == Status.InProgress) {
                    stepAndStatus.first to Status.Done
                } else if (stepAndStatus.first == step) {
                    step to Status.InProgress
                } else {
                    stepAndStatus
                }
            }
        )
        notifyObservers()
    }

    private suspend fun failCurrentStep(){

        if (SLOMO){
            delay(1000)
        }

        state = state.copy(
            progress = state.progress.map{ stepAndStatus ->
                if (stepAndStatus.second == Status.InProgress) {
                    stepAndStatus.first to Status.Failed
                } else {
                    stepAndStatus
                }
            }
        )
        notifyObservers()
    }
}

fun <F, S, S2> Either<F, S>.mapSuccess(
    block: (S) -> S2
): Either<F, S2> {
    return when (this) {
        is Either.Fail -> Either.fail(this.value)
        is Either.Success -> Either.success(block(this.value))
    }
}
