package foo.bar.clean.domain.features.init

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.features.CanError
import foo.bar.clean.domain.features.CanLoad
import foo.bar.clean.domain.features.State

data class PreInitState(
    override val loading: Boolean = false,
    val preInitProgress: Float = 0f,
    override val error: DomainError = DomainError.NoError,
) : State, CanLoad, CanError {
    fun preInitFailed() = !(error == DomainError.NoError || error == DomainError.UpgradeNag)
    fun preInitUninitialised() = preInitProgress == 0f
}
