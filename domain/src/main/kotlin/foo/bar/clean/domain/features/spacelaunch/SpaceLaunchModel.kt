package foo.bar.clean.domain.features.spacelaunch

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either.Fail
import co.early.fore.kt.core.type.Either.Success
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.ReadableState
import foo.bar.clean.domain.services.api.Launch
import foo.bar.clean.domain.services.api.LaunchService

class SpaceLaunchModel(
    private val launchService: LaunchService,
) : ReadableState<SpaceLaunchState>, Observable by ObservableImp() {

    override var state = SpaceLaunchState()
        private set

    /**
     * fetch the list of rocket launches from the launch service
     */
    fun refreshLaunchList() {

        Fore.i("refreshLaunchList()")

        if (state.loading) {
            return
        }

        state = state.copy(
            error = DomainError.NoError,
            loading = true
        )
        notifyObservers()

        launchMain {
            when (val result = launchService.refreshLaunchList()) {
                is Success -> {
                    handleSuccess(result.value)
                }

                is Fail -> {
                    handleFail(result.value)
                }
            }
        }
    }

    fun clearData() {

        Fore.i("clearData()")

        if (!state.loading) {
            state = SpaceLaunchState()
            notifyObservers()
        }
    }

    fun clearError() {

        Fore.i("clearError()")

        if (!state.loading) {
            state = state.copy(error = DomainError.NoError)
            notifyObservers()
        }
    }

    private fun handleSuccess(launches: List<Launch>) {

        val newLaunches = launches.mapNotNull {
            when (it) {
                is Launch.ALaunch -> it
                Launch.NoLaunch -> null
            }
        }.filter {
            it.patchThumbImgUrl.isNotBlank()
        }

        state = SpaceLaunchState(
            error = DomainError.NoError,
            loading = false,
            launches = newLaunches,
        )
        notifyObservers()
    }

    private fun handleFail(error: DomainError) {

        Fore.e("handleFail $error")

        state = state.copy(
            error = error,
            loading = false,
        )
        notifyObservers()
    }
}
