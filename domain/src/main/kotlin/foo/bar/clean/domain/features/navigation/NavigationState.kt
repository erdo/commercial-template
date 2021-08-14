package foo.bar.clean.domain.features.navigation

import foo.bar.clean.domain.services.api.Fruit
import foo.bar.clean.domain.features.CanLoad
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable

@Serializable
data class NavigationState(
    /**
     * backStack is the full history, plus the current location in the last position
     */
    @Serializable
    val backStack: List<Location>,
    @Serializable
    val currentLocationWillBeAddedToHistoryOnNextNavigation: Boolean = true,
    @Transient
    override val loading: Boolean = false,
) : State, CanLoad {

    init {
        require(backStack.isNotEmpty()) { "The backstack needs at least one location item that serves as the homepage" }
    }

    fun currentPage(): Location = backStack.last()
    fun canNavigateBack(): Boolean = backStack.size > 1
}

@Serializable
sealed class Location {
    @Serializable
    data object CounterLocation : Location()

    @Serializable
    sealed class TodoLocations : Location() {
        @Serializable
        data object TodoLocation : TodoLocations()
        @Serializable
        data object TodoAddLocation : TodoLocations()
        @Serializable
        data class TodoEditLocation(val index: Int) : TodoLocations()
    }

    @Serializable
    sealed class SpaceLaunchLocations : Location() {
        @Serializable
        data object SpaceLaunchLocation : SpaceLaunchLocations()
        @Serializable
        data class SpaceDetailLocation(val id: String) : SpaceLaunchLocations()
    }

    @Serializable
    data object FavouritesLocation : Location()

    @Serializable
    data object NavigationLocation : Location()

    @Serializable
    sealed class SettingsLocations : Location() {
        @Serializable
        data class SettingsLocation(val color: ULong? = null) : SettingsLocations()
        @Serializable
        data object SetColor : SettingsLocations()
    }

    @Serializable
    data object TicketLocation : Location()

    @Serializable
    data class FruitLocation(val overrideFruit: Fruit = Fruit.FruitNone) : Location()
}

val HOME_LOCATION = Location.CounterLocation
