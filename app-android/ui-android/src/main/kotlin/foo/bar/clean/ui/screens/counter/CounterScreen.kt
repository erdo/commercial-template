package foo.bar.clean.ui.screens.counter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import co.early.fore.compose.observeAsState
import foo.bar.clean.domain.features.counter.CounterModel
import foo.bar.clean.domain.features.counter.CounterState
import foo.bar.clean.domain.features.ReadableState
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerCounterScreen
import org.koin.compose.koinInject

@Composable
fun CounterScreen(
    counterStateProvider: ReadableState<CounterState> = (koinInject() as CounterModel),
    actionHandler: ActionHandlerCounterScreen = koinInject(),
) {

    val counterState by counterStateProvider.observeAsState { counterStateProvider.state }

    CounterView(
        counterState = counterState,
        perform = { action -> actionHandler.handle(action) },
    )
}
