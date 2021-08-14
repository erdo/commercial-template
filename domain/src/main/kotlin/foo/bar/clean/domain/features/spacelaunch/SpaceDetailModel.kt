package foo.bar.clean.domain.features.spacelaunch

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either.Fail
import co.early.fore.kt.core.type.Either.Success
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.Launch
import foo.bar.clean.domain.services.api.LaunchService
import foo.bar.clean.domain.features.ReadableState

class SpaceDetailModel(
    private val launchService: LaunchService,
) : ReadableState<SpaceDetailState>, Observable by ObservableImp() {

    override var state = SpaceDetailState()
        private set

    /**
     * set launch
     */
    fun setLaunch(id: String) {

        Fore.i("setLaunch() id:$id")

        if (state.loading) {
            return
        }

        state = state.copy(
            error = DomainError.NoError,
            loading = false,
            launch = Launch.ALaunch(id = id)
        )
        notifyObservers()
    }

    /**
     * fetch launch detail
     */
    fun fetchLaunchDetail() {

        Fore.i("fetchLaunchDetail()")

        if (state.loading || state.launch == Launch.NoLaunch) {
            return
        }

        state = state.copy(
            error = DomainError.NoError,
            loading = true
        )
        notifyObservers()

        launchMain {
            when (val result = launchService.refreshLaunchDetail(state.launchId)) {
                is Success -> {
                    handleSuccess(result.value)
                }

                is Fail -> {
                    handleFail(result.value)
                }
            }
        }
    }

    /**
     * book launch
     */
    fun bookLaunch() {

        Fore.i("bookLaunch()")

        if (state.loading || state.launch == Launch.NoLaunch) {
            return
        }

        state = state.copy(
            error = DomainError.NoError,
            loading = true
        )
        notifyObservers()

        launchMain {
            when (val result = launchService.bookTrip(state.launchId)) {
                is Success -> {
                    result.value.let { bookingResult ->
                        if (bookingResult.success) {
                            state = state.copy(
                                loading = false,
                                launch = (state.launch as Launch.ALaunch).copy(isBooked = true)
                            )
                            notifyObservers()
                        } else {
                            Fore.e("booking failed: ${bookingResult.message}")
                            handleFail(DomainError.RetryLater)
                        }
                    }
                }

                is Fail -> {
                    handleFail(result.value)
                }
            }
        }
    }

    /**
     * cancel the current launch booking
     */
    fun cancelLaunch() {

        Fore.i("cancelLaunch()")

        if (state.loading || state.launch == Launch.NoLaunch) {
            return
        }

        state = state.copy(
            error = DomainError.NoError,
            loading = true
        )
        notifyObservers()

        launchMain {
            when (val result = launchService.cancelTrip(state.launchId)) {
                is Success -> {
                    result.value.let { cancelResult ->
                        if (cancelResult.success) {
                            state = state.copy(
                                loading = false,
                                launch = (state.launch as Launch.ALaunch).copy(isBooked = false)
                            )
                            notifyObservers()
                        } else {
                            Fore.e("cancel booking failed: ${cancelResult.message}")
                            handleFail(DomainError.RetryLater)
                        }
                    }
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
            state = SpaceDetailState()
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

    private fun handleSuccess(launch: Launch.ALaunch) {

        state = SpaceDetailState(
            error = DomainError.NoError,
            loading = false,
            launch = launch,
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
