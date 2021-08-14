package foo.bar.clean.domain.features.auth

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

const val NO_SESSION = ""

@Serializable
data class AuthState(
    val token: String = NO_SESSION,
    @Transient
    val error: DomainError = DomainError.NoError,
    @Transient
    val loading: Boolean = false,
) : State {
    val signedIn = token != NO_SESSION
}
