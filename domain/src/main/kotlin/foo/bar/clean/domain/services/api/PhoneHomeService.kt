package foo.bar.clean.domain.services.api

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import kotlinx.serialization.Serializable

interface PhoneHomeService {
    suspend fun phoneHome(): Either<DomainError, PhoneHomeResult>
}

@Serializable
data class PhoneHomeResult(
    val endpointsUrl: String,
    val minSupportedVersion: String,
    val minNoNagVersion: String,
)
