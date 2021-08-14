package foo.bar.clean.data.db.service.todo

import foo.bar.clean.data.db.DriverFactory
import foo.bar.clean.data.db.todoitem.TodoListDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

class TodoItemDb(df: DriverFactory) {
    val db = TodoListDatabase(df.createDriver(TodoListDatabase.Schema, "TodoListDatabase"))
    val dispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
}
