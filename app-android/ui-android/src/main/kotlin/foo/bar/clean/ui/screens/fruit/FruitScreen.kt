package foo.bar.clean.ui.screens.fruit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import co.early.fore.compose.observeAsState
import foo.bar.clean.domain.features.fruit.FruitModel
import foo.bar.clean.domain.features.fruit.FruitState
import foo.bar.clean.domain.features.ReadableState
import foo.bar.clean.ui.actionhandlers.screens.ActionHandlerFruitScreen
import org.koin.compose.koinInject

@Composable
fun FruitScreen(
    fruitStateProvider: ReadableState<FruitState> = (koinInject() as FruitModel),
    actionHandler: foo.bar.clean.ui.actionhandlers.screens.ActionHandlerFruitScreen = koinInject()
) {

    val fruitState by fruitStateProvider.observeAsState { fruitStateProvider.state }

    FruitView(
        fruitState = fruitState,
        perform = { action -> actionHandler.handle(action) },
    )
}
