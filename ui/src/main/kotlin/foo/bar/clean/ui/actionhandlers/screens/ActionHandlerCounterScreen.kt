package foo.bar.clean.ui.actionhandlers.screens

import co.early.fore.kt.core.delegate.Fore
import foo.bar.clean.domain.features.counter.CounterModel
import foo.bar.clean.ui.actionhandlers.Act
import foo.bar.clean.ui.actionhandlers.GlobalActionHandler
import foo.bar.clean.ui.actionhandlers.koinInject

class ActionHandlerCounterScreen(
    private val counterModel: CounterModel = koinInject(),
) : GlobalActionHandler<Act.ScreenCounter>() {

    override fun __handle(act: Act.ScreenCounter) {

        Fore.i("_handle ScreenCounter Action: $act")

        when (act) {
            Act.ScreenCounter.DecreaseCounter -> counterModel.decrease()
            Act.ScreenCounter.IncreaseCounter -> counterModel.increase()
        }
    }
}
