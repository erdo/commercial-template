package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import kotlinx.serialization.Serializable

interface AuthService {
    suspend fun login(email: String): Either<DomainError, Auth>
}

@Serializable
data class Auth(
    val token: String? = null,
)
