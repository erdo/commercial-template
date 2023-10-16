package foo.bar.clean.domain.features.todo

import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.core.type.Either
import co.early.persista.PerSista
import foo.bar.clean.domain.SLOMO
import foo.bar.clean.domain.features.ReadableState
import foo.bar.clean.domain.services.db.TodoItem
import foo.bar.clean.domain.services.db.TodoItemService
import kotlinx.coroutines.delay

/**
 * Observable model that encapsulates a todolist and is backed by some persistent storage and a db
 *
 * The state has a list of todoItems, the mutation functions allow you to add a todoItem,
 * select an item as done etc.
 *
 * An easy way to get an overview is to see the Structure view on the left for this model,
 * and to look at the associated TodoState class
 *
 * - UserOptions (currentItemIndex, show done items, etc) are saved persistently in a json blob,
 * - The list of TodoItems (label, isDone, creationTime, etc) is kept in a db
 */
class TodoModel(
    private val todoItemService: TodoItemService,
    private val perSista: PerSista,
) : ReadableState<TodoState>, Observable by ObservableImp() {

    override var state = TodoState(loading = true)
        private set

    init {
        launchMain {

            if (SLOMO) {
                delay(500)
            }

            val userOptions: UserOptions = perSista.read(UserOptions())
            reloadDd(userOptions.includeDone)
        }
    }

    fun toggleShowDone() {

        Fore.i("toggleShowDone() loading:${state.loading}")

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchMain {
            val newUserOptions =
                state.userOptions.copy(includeDone = !state.userOptions.includeDone)
            val persistedNewUserOptions = perSista.write(newUserOptions)
            reloadDd(persistedNewUserOptions.includeDone)
        }
    }

    fun toggleDoneForItem(index: Int) {

        Fore.i("toggleDoneForItem() loading:${state.loading} index:$index")

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        val newItem = state.items.getOrNull(index)?.run {
            copy(done = !done)
        }

        launchMain {
            if (newItem != null) {
                when (val result = todoItemService.updateTodoItem(newItem)) {
                    is Either.Fail -> {
                        Fore.e("error updating item in db: ${result.value}")
                        state = state.copy(loading = false)
                        notifyObservers()
                    }

                    is Either.Success -> {
                        Fore.i("success updating item in db: ${result.value}")
                        reloadDd(state.userOptions.includeDone)
                    }
                }
            } else {
                Fore.e("error selecting item, index [$index] not valid for size: ${state.items.size}")
                state = state.copy(loading = false)
                notifyObservers()
            }
        }
    }

    fun createItem(label: String) {

        Fore.i("createItem() loading:${state.loading} label:$label")

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchMain {

            if (SLOMO) {
                delay(500)
            }

            when (val result = todoItemService.createTodoItem(label)) {
                is Either.Fail -> {
                    Fore.e("error creating item in db: ${result.value}")
                    state = state.copy(loading = false)
                    notifyObservers()
                }

                is Either.Success -> {
                    Fore.i("success creating item in db, id:${result.value.id}")
                    reloadDd(state.userOptions.includeDone)
                }
            }
        }
    }

    fun updateItem(item: TodoItem) {

        Fore.i("updateCurrentItem() loading:${state.loading} id:${item.id} done:${item.done} label:${item.label}")

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchMain {
            when (val result = todoItemService.updateTodoItem(item)) {
                is Either.Fail -> {
                    Fore.e("error updating item in db: ${result.value}")
                    state = state.copy(loading = false)
                    notifyObservers()
                }

                is Either.Success -> {
                    Fore.i("success updating item in db, id:${result.value}")
                    reloadDd(state.userOptions.includeDone)
                }
            }
        }
    }

    fun deleteItem(index: Int) {

        Fore.i("deleteItem() loading:${state.loading} index:$index")

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        val itemToDelete = state.items.getOrNull(index)

        launchMain {
            if (itemToDelete != null) {
                when (val result = todoItemService.deleteTodoItem(itemToDelete.id)) {
                    is Either.Fail -> {
                        Fore.e("error deleting item in db: ${result.value}")
                        state = state.copy(loading = false)
                        notifyObservers()
                    }

                    is Either.Success -> {
                        Fore.i("success deleting item in db: ${result.value}")
                        reloadDd(state.userOptions.includeDone)
                    }
                }
            } else {
                Fore.e("error deleting item, index [$index] not valid for size: ${state.items.size}")
                state = state.copy(loading = false)
                notifyObservers()
            }
        }
    }

    fun deleteMultipleItems(ids: List<Long>) {

        Fore.i("deleteMultipleItems() loading:${state.loading} ids:$ids")

        if (state.loading) {
            return
        }

        state = state.copy(loading = true)
        notifyObservers()

        launchMain {
            when (val result = todoItemService.deleteMultiple(ids)) {
                is Either.Fail -> {
                    Fore.e("error deleting multiple items from db: ${result.value}")
                    state = state.copy(loading = false)
                    notifyObservers()
                }

                is Either.Success -> {
                    Fore.i("success deleting multiple items from db (all deleted: ${result.value})")
                    reloadDd(state.userOptions.includeDone)
                }
            }
        }
    }

    private suspend fun reloadDd(includeDone: Boolean, continueLoadingAfter: Boolean = false) {

        state = state.copy(loading = true)
        notifyObservers()

        val result = if (includeDone) {
            todoItemService.selectAll()
        } else {
            todoItemService.selectAllDone(false)
        }

        state = when (result) {
            is Either.Fail -> {
                Fore.e("error selecting items from db: ${result.value}")
                state.copy(loading = continueLoadingAfter)
            }

            is Either.Success -> {
                Fore.i("success selected [${result.value.first.size}] items from db")
                state.copy(
                    loading = continueLoadingAfter,
                    items = result.value.first,
                    totalItems = result.value.second.toInt(), // not worried about more than Int.MAX_VALUE items
                    userOptions = state.userOptions.copy(includeDone = includeDone),
                )
            }
        }
        notifyObservers()
    }
}
