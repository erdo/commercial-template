package foo.bar.clean.domain.features.config

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either
import co.early.persista.PerSista
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.ConfigService
import foo.bar.clean.domain.features.ReadableStateCanLoadCanError

/**
 * This is an observable model that tries to fetch its data from a service, if the service
 * fails, an error is set on the state, and the previously persisted data are loaded,
 * if that also fails the state will end up with a default config and the original service error
 * indicating why it failed
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class ConfigModel(
    private val configService: ConfigService,
    private val perSista: PerSista,
) : ReadableStateCanLoadCanError<ConfigState>, Observable by ObservableImp() {

    override var state = ConfigState()
        private set

    override fun load() {

        Fore.i("load()")

        if (state.loading){
            return
        }

        state = state.copy(loading = true)
        Fore.i("config model is loading")
        notifyObservers()

        launchMain {
            when (val result = configService.fetchConfig()) {
                is Either.Fail -> useCachedState(result.value)
                is Either.Success -> updateState(ConfigState(config = result.value))
            }
        }
    }

    private fun useCachedState(error: DomainError) {
        Fore.i("useCachedState() $error")
        perSista.read(ConfigState(error = error)) { readState ->
            Fore.d("notifyObservers.....1")
            state = readState
            notifyObservers()
        }
    }

    private fun updateState(newState: ConfigState) {
        Fore.i("updateState()")
        state = newState
        perSista.write(state) {
            Fore.d("notifyObservers.....2")
            notifyObservers()
        }
    }
}
