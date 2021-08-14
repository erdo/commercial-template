package foo.bar.clean.domain.services.db

import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError

interface TodoItemService {
    suspend fun createTodoItem(label: String): Either<DomainError, TodoItem>
    suspend fun insertTodoItem(todoItem: TodoItem): Either<DomainError, TodoItem>
    suspend fun insertManyTodoItems(todoItems: List<TodoItem>): Either<DomainError, List<TodoItem>>
    suspend fun selectTodoItem(id: Long): Either<DomainError, TodoItem?>
    suspend fun selectAll(): Either<DomainError, Pair<List<TodoItem>, Long>>
    suspend fun selectAllLimit(limit: Long): Either<DomainError, Pair<List<TodoItem>, Long>>
    suspend fun selectAllDone(done: Boolean): Either<DomainError, Pair<List<TodoItem>, Long>>
    suspend fun updateTodoItem(todoItem: TodoItem): Either<DomainError, Boolean>
    suspend fun deleteTodoItem(id: Long): Either<DomainError, Boolean>
    suspend fun deleteMultiple(ids: List<Long>): Either<DomainError, Boolean>
}

data class TodoItem(
    val createdTimestampMs: Long,
    val label: String,
    val done: Boolean,
    val id: Long = 0,
)
