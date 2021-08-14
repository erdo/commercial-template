package foo.bar.clean.domain.features.config

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.Config
import foo.bar.clean.domain.features.CanError
import foo.bar.clean.domain.features.CanLoad
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ConfigState(
    @Serializable
    val config: Config = Config(),
    @Transient
    override val error: DomainError = DomainError.NoError,
    @Transient
    override val loading: Boolean = false,
) : State, CanLoad, CanError