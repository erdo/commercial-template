package foo.bar.clean.ui.actionhandlers.screens

import co.early.fore.kt.core.delegate.Fore
import foo.bar.clean.domain.features.navigation.Location.TodoLocations.TodoAddLocation
import foo.bar.clean.domain.features.navigation.Location.TodoLocations.TodoEditLocation
import foo.bar.clean.domain.features.navigation.NavigationModel
import foo.bar.clean.domain.features.todo.TodoModel
import foo.bar.clean.ui.actionhandlers.Act
import foo.bar.clean.ui.actionhandlers.GlobalActionHandler
import foo.bar.clean.ui.actionhandlers.koinInject

class ActionHandlerTodoScreen(
    private val todoModel: TodoModel = koinInject(),
    private val navigationModel: NavigationModel = koinInject(),
) : GlobalActionHandler<Act.ScreenTodo>() {

    override fun __handle(act: Act.ScreenTodo) {

        Fore.i("_handle ScreenTodo Action: $act busy:${todoModel.state.loading}")

        if (todoModel.state.loading) {
            return
        }

        when (act) {
            is Act.ScreenTodo.ToEditScreen -> navigationModel.navigateTo(TodoEditLocation(act.index))
            is Act.ScreenTodo.ToggleDone -> todoModel.toggleDoneForItem(act.index)
            Act.ScreenTodo.ToggleShowDone -> todoModel.toggleShowDone()
            is Act.ScreenTodo.ItemDelete -> todoModel.deleteItem(act.index)
            Act.ScreenTodo.ToAddScreen -> navigationModel.navigateTo(TodoAddLocation)
            is Act.ScreenTodo.UpdateThenBack -> {
                todoModel.updateItem(act.item)
                navigationModel.popBackStack()
            }
            is Act.ScreenTodo.CreateThenBack -> {
                todoModel.createItem(act.label)
                navigationModel.popBackStack()
            }
        }
    }
}
