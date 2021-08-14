package foo.bar.clean.ui.actionhandlers.screens

import co.early.fore.kt.core.delegate.Fore
import foo.bar.clean.domain.features.navigation.Location
import foo.bar.clean.domain.features.navigation.NavigationModel
import foo.bar.clean.domain.features.settings.SettingsModel
import foo.bar.clean.ui.actionhandlers.Act
import foo.bar.clean.ui.actionhandlers.GlobalActionHandler
import foo.bar.clean.ui.actionhandlers.koinInject

class ActionHandlerSettingsScreen(
    private val settingsModel: SettingsModel = koinInject(),
    private val navigationModel: NavigationModel = koinInject(),
) : GlobalActionHandler<Act.ScreenSettings>() {

    override fun __handle(act: Act.ScreenSettings) {

        Fore.i("_handle ScreenSettings Action: $act")

        when (act) {
            is Act.ScreenSettings.SetDarkMode -> settingsModel.setDarkMode(act.darkMode)
            is Act.ScreenSettings.SetColorAndBack -> navigationModel.popBackStack {
                when(it){
                    is Location.SettingsLocations.SettingsLocation -> {
                        it.copy(color = act.color)
                    }
                    else -> it
                }
            }
            Act.ScreenSettings.ToSetColorScreen -> navigationModel.navigateTo(
                Location.SettingsLocations.SetColor
            )
        }
    }
}
