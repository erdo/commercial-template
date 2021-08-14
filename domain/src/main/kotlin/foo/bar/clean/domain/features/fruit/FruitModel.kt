package foo.bar.clean.domain.features.fruit

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.Fruit
import foo.bar.clean.domain.services.api.FruitService
import foo.bar.clean.domain.features.ReadableState

/**
 * This is a basic observable model that publishes a "Fruit" state, this state is mutated (the
 * fruit changed) as a result of network calls made on a separate coroutine and thread. The state
 * is not persisted, for an example model that persists its state, please see CounterModel
 *
 * The purpose of this model is to fetch fruit from the network
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class FruitModel(
    private val fruitService: FruitService,
) : ReadableState<FruitState>, Observable by ObservableImp() {

    override var state = FruitState()
        private set

    fun refreshFruit() {

        Fore.i("refreshFruit() t:" + Thread.currentThread())

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchMain {
            Fore.i("about to call fruit service t:" + Thread.currentThread())
            when (val result = fruitService.getFruits()) {
                is Either.Fail -> handleFailure(result.value)
                is Either.Success -> handleSuccess(result.value)
            }
        }
    }

    fun refreshFruitForceFail() {

        Fore.i("refreshFruitForceFail() t:" + Thread.currentThread())

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchMain {
            Fore.i("about to call fruit service t:" + Thread.currentThread())
            when (val result = fruitService.getFruitsForceFailure()) {
                is Either.Fail -> handleFailure(result.value)
                is Either.Success -> handleSuccess(result.value)
            }
        }
    }

    private fun handleSuccess(fruit: Fruit) {
        Fore.i("handleSuccess() t:" + Thread.currentThread())
        state = state.copy(
            fruit = fruit,
            error = DomainError.NoError,
            loading = false
        )
        notifyObservers()
    }

    private fun handleFailure(error: DomainError) {
        Fore.i("handleFailure() t:" + Thread.currentThread())
        state = state.copy(
            fruit = Fruit.FruitNone,
            error = error,
            loading = false
        )
        notifyObservers()
    }

    private fun handleSuccess(fruitList: List<Fruit>) {
        handleSuccess(fruitList.takeRnd(Fruit.FruitNone))
    }
}
