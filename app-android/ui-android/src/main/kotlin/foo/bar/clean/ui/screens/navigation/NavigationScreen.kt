package foo.bar.clean.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import foo.bar.clean.domain.features.navigation.NavigationModel
import foo.bar.clean.domain.features.navigation.NavigationState
import foo.bar.clean.domain.features.ReadableState
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerNavigationScreen
import foo.bar.clean.ui.common.toState
import org.koin.compose.koinInject

@Composable
fun NavigationScreen(
    navigationStateProvider: ReadableState<NavigationState> = (koinInject() as NavigationModel),
    actionHandler: foo.bar.clean.ui.actionhandlers.screens.ActionHandlerNavigationScreen = koinInject()
) {

    val navigationState by navigationStateProvider.toState()

    NavigationView(
        viewState = navigationState,
        perform = { action -> actionHandler.handle(action) },
    )
}
