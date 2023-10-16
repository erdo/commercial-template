package foo.bar.clean.domain.features.auth

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either.Fail
import co.early.fore.kt.core.type.Either.Success
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.ReadableState
import foo.bar.clean.domain.services.api.AuthService


/**
 * logs the user in / out (gets a session token from the server / disposes of it)
 */
class AuthModel(
    private val authService: AuthService,
) : ReadableState<AuthState>, Observable by ObservableImp() {

    override var state = AuthState()
        private set

    private val defaultEmail = "test${java.util.Random().nextInt(999)}@test.com"

    /**
     * get a session token from the service
     */
    fun signIn(
        email: String = defaultEmail,
    ) {

        Fore.i("signIn() email:$email")

        if (state.loading) {
            return
        }

        if (email.isBlank()) {
            state = state.copy(error = DomainError.BlankField)
            notifyObservers()
            return
        }

        state = AuthState(loading = true)
        notifyObservers()

        launchMain {
            when (val result = authService.login(email)) {
                is Success -> {
                    state = AuthState(token = result.value.token ?: NO_SESSION)
                    notifyObservers()
                }

                is Fail -> {
                    state = AuthState(error = result.value)
                    notifyObservers()
                }
            }
        }
    }

    /**
     * drops the sessions token and informs any observers
     */
    fun signOut() {

        Fore.i("signOut()")

        if (state.loading) {
            return
        }

        state = AuthState()
        notifyObservers()
    }
}
