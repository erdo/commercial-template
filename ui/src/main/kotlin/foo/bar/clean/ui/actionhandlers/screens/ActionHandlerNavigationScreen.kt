package foo.bar.clean.ui.actionhandlers.screens

import co.early.fore.kt.core.delegate.Fore
import foo.bar.clean.domain.features.navigation.NavigationModel
import foo.bar.clean.ui.actionhandlers.Act
import foo.bar.clean.ui.actionhandlers.GlobalActionHandler
import foo.bar.clean.ui.actionhandlers.koinInject

class ActionHandlerNavigationScreen(
    private val navigationModel: NavigationModel = koinInject(),
) : GlobalActionHandler<Act.ScreenNavigation>() {

    override fun __handle(act: Act.ScreenNavigation) {

        Fore.i("_handle ScreenNavigation Action: $act")

        when (act) {
            is Act.ScreenNavigation.UpdateBackstack -> {
                navigationModel.updateBackStack(newBackStack = act.backstack)
            }
        }
    }
}
