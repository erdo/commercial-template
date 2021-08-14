package foo.bar.clean.data

import foo.bar.clean.data.api.rest.service.endpoints.EndpointKey
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.DomainError.CheckAccount
import foo.bar.clean.domain.DomainError.RetryAfterLogin
import foo.bar.clean.domain.DomainError.RetryAfterNetworkCheck
import foo.bar.clean.domain.DomainError.RetryLater
import kotlinx.serialization.Serializable

@Serializable
sealed class DataError(val resolution: DomainError) {

    // generic errors

    object Misc : DataError(RetryLater)
    object Network : DataError(RetryAfterNetworkCheck)
    object SecurityUnknown : DataError(RetryAfterNetworkCheck)
    object Server : DataError(RetryLater)
    object AlreadyExecuted : DataError(RetryLater)
    object Client : DataError(RetryLater)
    object RateLimited : DataError(RetryLater)
    object SessionTimedOut : DataError(RetryAfterLogin)
    object Busy : DataError(RetryLater)
    data class EndpointMissing(val key: EndpointKey) : DataError(RetryAfterNetworkCheck)

    // feature specific errors

    object FruitUserLoginCredentialsIncorrect : DataError(RetryAfterLogin)
    object FruitUserLocked : DataError(CheckAccount)
    object FruitUserNotEnabled : DataError(CheckAccount)
}
