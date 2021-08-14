package foo.bar.clean.ui.screens.todo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTodoScreen
import foo.bar.clean.ui.common.showKeyboard
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoAddScreen(
    actionHandler: foo.bar.clean.ui.actionhandlers.screens.ActionHandlerTodoScreen = koinInject(),
) {
    TodoAddView(
        perform = { action -> actionHandler.handle(action) }
    )
}
