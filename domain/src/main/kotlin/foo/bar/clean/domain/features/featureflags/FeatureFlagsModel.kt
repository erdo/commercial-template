package foo.bar.clean.domain.features.featureflags

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either
import co.early.persista.PerSista
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.FeatureFlagsService
import foo.bar.clean.domain.features.ReadableStateCanLoadCanError

/**
 * This is an observable model that tries to fetch its data from a service, if the service
 * fails, an error is set on the state, and the previously persisted data are loaded,
 * if that also fails the state will end up with an empty feature flag map and the original service
 * error indicating why it failed
 *
 * Open the Structure pane on the left to get an overview of the public API for this model
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class FeatureFlagsModel(
    private val featureFlagsService: FeatureFlagsService,
    private val perSista: PerSista,
) : ReadableStateCanLoadCanError<FeatureFlagsState>, Observable by ObservableImp() {

    override var state = FeatureFlagsState()
        private set

    override fun load() {

        Fore.i("load()")

        if (state.loading){
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchMain {
            when (val result = featureFlagsService.fetchFlags()) {
                is Either.Fail -> useCachedState(result.value)
                is Either.Success -> updateState(FeatureFlagsState(features = result.value))
            }
        }
    }

    private fun useCachedState(error: DomainError) {
        perSista.read(FeatureFlagsState(error = error)) { readState ->
            state = readState
            notifyObservers()
        }
    }

    private fun updateState(newState: FeatureFlagsState) {
        state = newState
        perSista.write(state) {
            notifyObservers()
        }
    }
}
