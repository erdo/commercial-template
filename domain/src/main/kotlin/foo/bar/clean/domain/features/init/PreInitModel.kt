package foo.bar.clean.domain.features.init

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either
import co.early.fore.kt.core.type.Either.Companion.fail
import co.early.fore.kt.core.type.carryOn
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.features.ReadableStateCanLoad
import foo.bar.clean.domain.features.meta.MetaModel
import foo.bar.clean.domain.features.observeUntilLoaded
import foo.bar.clean.domain.features.settings.SettingsModel
import foo.bar.clean.domain.services.api.EndpointsService
import foo.bar.clean.domain.services.api.PhoneHomeService
import foo.bar.clean.domain.utils.version.Version
import kotlinx.coroutines.delay

/**
 * Things that need to run first before anything else. For this app that's: the PhoneHome service
 * and then the Endpoints Service
 *
 * Copyright © 2015-2023 early.co. All rights reserved.
 */
class PreInitModel(
    private val metaModel: MetaModel, // this gives us the current app version number
    private val phoneHomeService: PhoneHomeService, // this returns the supported versions and the URL to get the rest of the endpoints
    private val settingsModel: SettingsModel, // locally stored user settings
    private val endpointsService: EndpointsService, // this returns set the endpoints used in the data layer
) : ReadableStateCanLoad<PreInitState>, Observable by ObservableImp() {

    override var state = PreInitState()
        private set

    /**
     * making 2 calls here in sequence: the phone home call, and the fetch endpoints call
     */
    override fun load() {

        Fore.d("load")

        if (state.loading) {
            return
        }

        state = PreInitState(loading = true, preInitProgress = 0.2f)
        notifyObservers()

        launchMain {

            settingsModel.observeUntilLoaded()

            // fake delay
            if (SLOMO) {
                delay(1500)
            }

            metaModel.observeUntilLoaded()

            state = state.copy(preInitProgress = 0.4f)
            notifyObservers()

            //val currentVersion = Version(metaModel.state.meta.version) // Version("1.0.0")
            val currentVersion = Version("1.0.0")
            val phoneHomeResult = phoneHomeService.phoneHome()
            Fore.d("phoneHome returned")

            var nagForUpgradeAtEnd = false

            val decoratedPhoneHomeResult = when (phoneHomeResult) {
                is Either.Fail -> phoneHomeResult
                is Either.Success -> {
                    when {
                        currentVersion < Version(phoneHomeResult.value.minSupportedVersion) -> {
                            fail(DomainError.UpgradeForce)
                        }

                        currentVersion < Version(phoneHomeResult.value.minNoNagVersion) -> {
                            nagForUpgradeAtEnd = true
                            phoneHomeResult
                        }

                        else -> phoneHomeResult
                    }
                }
            }

            state = state.copy(preInitProgress = 0.8f)
            notifyObservers()

            val endpointsResult = decoratedPhoneHomeResult.carryOn {
                endpointsService.fetchEndpoints(it.endpointsUrl)
            }
            Fore.d("fetchEndpoints returned")

            when (endpointsResult) {
                is Either.Fail -> {
                    state = state.copy(
                        loading = false,
                        error = endpointsResult.value,
                        preInitProgress = 1f
                    )
                }

                is Either.Success -> {
                    state = state.copy(
                        loading = false,
                        error = if (nagForUpgradeAtEnd) {
                            DomainError.UpgradeNag
                        } else DomainError.NoError,
                        preInitProgress = 1f,
                    )
                }
            }

            Fore.d("loading complete")

            notifyObservers()
        }
    }

    fun acknowledgeNag() {
        if (!state.loading && state.error == DomainError.UpgradeNag) {
            state = state.copy(error = DomainError.NoError)
            notifyObservers()
        }
    }
}
