package foo.bar.clean.domain.features.settings

import foo.bar.clean.domain.features.CanLoad
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SettingsState(
    val darkMode: DarkMode = DarkMode.System,
    @Transient
    override val loading: Boolean = false,
) : State, CanLoad

@Serializable
sealed class DarkMode {
    @Serializable
    object Dark : DarkMode()

    @Serializable
    object Light : DarkMode()

    @Serializable
    object System : DarkMode()

    /**
     * Just so we can visualise the state easier on the UI,
     * feel free to remove this function
     */
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
