package foo.bar.clean.domain.features.counter

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchIO
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.persista.PerSista
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.features.config.ConfigModel
import foo.bar.clean.domain.features.ReadableState
import kotlinx.coroutines.delay

/**
 * This is a basic observable model that exposes its state and persists that state across rotation
 * or process death with PerSista (PerSista stores data classes as json on the file system). For a
 * basic model which doesn't persist its state see NetworkModel or TicketModel.
 *
 * The purpose of this model is to maintain a counter which the user can increment or decrement
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class CounterModel(
    configModel: ConfigModel,
    private val perSista: PerSista,
) : ReadableState<CounterState>, Observable by ObservableImp() {

    private val max = configModel.state.config.counter.max
    private val min = configModel.state.config.counter.min
    override var state = CounterState(amount = min, max = max, min = min, loading = true)
        private set

    init {
        launchIO {
            if (SLOMO){
                delay(1000)
            }
            perSista.read(state) {
                state = it.copy(
                    loading = false,
                    amount = it.amount.coerceIn(min, max), // coerce in case config has changed since the last time
                    max = max,
                    min = min,
                )
                notifyObservers()
            }
        }
    }

    fun increase() {
        if (state.canIncrease()) {
            perSista.write(state.copy(amount = state.amount + 1)) {
                Fore.i("counter increased to:${it.amount}")
                state = it
                notifyObservers()
            }
        }
    }

    fun decrease() {
        if (state.canDecrease()) {
            perSista.write(state.copy(amount = state.amount - 1)) {
                Fore.i("counter decreased to:${it.amount}")
                state = it
                notifyObservers()
            }
        }
    }
}
