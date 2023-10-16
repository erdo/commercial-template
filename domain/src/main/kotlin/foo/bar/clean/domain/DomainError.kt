package foo.bar.clean.domain

import kotlinx.serialization.Serializable

@Serializable
sealed class DomainError {
    object NoError : DomainError()
    object RetryLater : DomainError()
    object RetryAfterNetworkCheck : DomainError()
    object RetryAfterLogin : DomainError()
    object CheckAccount : DomainError()
    object BlankField : DomainError()
    object NoData : DomainError()
    object UpgradeNag : DomainError()
    object UpgradeForce : DomainError()

    /**
     * Just so we can visualise the state easier on the UI,
     * feel free to remove this function
     */
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
