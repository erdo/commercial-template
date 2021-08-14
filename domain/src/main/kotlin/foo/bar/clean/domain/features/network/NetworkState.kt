package foo.bar.clean.domain.features.network

import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable

/**
 * NB: don't use this state to decide whether you should attempt a network
 * call or not: ALWAYS attempt the call anyway and handle the failure if necessary
 */
@Serializable
data class NetworkState(
    /**
     * backStack is the full history, plus the current location in the last position
     */
    @Serializable
    val availability: Availability = Availability.Available,
) : State

@Serializable
sealed class Availability {
    data object Unavailable : Availability()
    data object Available : Availability()
}
