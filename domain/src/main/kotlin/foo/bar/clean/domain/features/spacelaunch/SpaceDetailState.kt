package foo.bar.clean.domain.features.spacelaunch

import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.api.Launch
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

const val UNKNOWN_ID = ""

@Serializable
data class SpaceDetailState(
    @Transient
    val error: DomainError = DomainError.NoError,
    @Transient
    val loading: Boolean = false,
    @Serializable
    val launch: Launch = Launch.NoLaunch,
) : State {
    val launchId = if (launch is Launch.ALaunch) launch.id else UNKNOWN_ID
    val isBooked = if (launch is Launch.ALaunch) launch.isBooked else false
    val patchImgUrl = if (launch is Launch.ALaunch) launch.patchImgUrl else "unknown patchImgUrl"
    val site = if (launch is Launch.ALaunch) launch.site else "unknown site"
}
