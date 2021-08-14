package foo.bar.clean.domain.features.todo

import foo.bar.clean.domain.services.db.TodoItem
import foo.bar.clean.domain.features.State
import kotlinx.serialization.Serializable

data class TodoState(
    val items: List<TodoItem> = emptyList(),
    val userOptions: UserOptions = UserOptions(),
    val totalItems: Int = 0, // including items which are done
    @Transient
    val loading: Boolean = false,
) : State {
    fun hiddenItems(): Int {
        return items.size - totalItems
    }
}

@Serializable
data class UserOptions(
    val includeDone: Boolean = true,
)
