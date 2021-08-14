package foo.bar.clean.domain.features.favourites

import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class FavouritesState(
    @Serializable
    val favourites: List<Feature> = emptyList(),
    @Transient
    val loading: Boolean = false,
): State {
    fun isFavourite(feature: Feature): Boolean = favourites.contains(feature)
}

@Serializable
sealed class Feature {
    @Serializable
    object Counter: Feature()
    @Serializable
    object Todo: Feature()
    @Serializable
    object SpaceLaunch: Feature()
    @Serializable
    object Favourites: Feature()
    @Serializable
    object Settings: Feature()
    @Serializable
    object Fruit: Feature()
    @Serializable
    object Ticket: Feature()
    @Serializable
    object Weather: Feature()
    @Serializable
    object Network: Feature()

    /**
     * Just so we can visualise the state easier on the UI,
     * feel free to remove this function
     */
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
