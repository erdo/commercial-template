package foo.bar.clean.ui.screens.todo

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTodoScreen
import org.koin.compose.koinInject

@Composable
fun TodoAddScreen(
    actionHandler: ActionHandlerTodoScreen = koinInject(),
) {
    TodoAddView(
        perform = { action -> actionHandler.handle(action) }
    )
}
