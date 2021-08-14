package foo.bar.clean.domain.features.spacelaunch

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.Launch
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SpaceLaunchState(
    @Transient
    val error: DomainError = DomainError.NoError,
    @Transient
    val loading: Boolean = false,
    @Serializable
    val launches: List<Launch.ALaunch> = emptyList(),
) : State
