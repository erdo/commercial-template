package foo.bar.clean.data.db.service.todo

import co.early.fore.core.time.SystemTimeWrapper
import co.early.fore.kt.core.coroutine.awaitCustom
import co.early.fore.kt.core.type.Either
import foo.bar.clean.domain.DomainError
import foo.bar.clean.domain.services.db.TodoItem
import foo.bar.clean.domain.services.db.TodoItemService

class TodoItemServiceImp(
    todoItemDb: TodoItemDb,
    private val systemTimeWrapper: SystemTimeWrapper,
) : TodoItemService {

    private val query = todoItemDb.db.todoItemOperationsQueries
    private val dispatcher = todoItemDb.dispatcher

    /**
     * returns the item created
     */
    override suspend fun createTodoItem(label: String): Either<DomainError, TodoItem> {
        return awaitCustom(dispatcher) {
            query.run {
                try {
                    transactionWithResult {
                        createItem(
                            label = label,
                            creation_timestamp_ms = systemTimeWrapper.currentTimeMillis()
                        )
                        if (changes().executeAsOne() > 0) {
                            val id = lastInsertRowId().executeAsOne()
                            select(id).executeAsOneOrNull()?.toDomain()?.let {
                                Either.success(it)
                            } ?: Either.fail(DomainError.RetryLater)
                        } else {
                            Either.fail(DomainError.RetryLater)
                        }
                    }
                } catch (t: Throwable) {
                    Either.fail(DomainError.RetryLater)
                }
            }
        }
    }

    /**
     * returns the item inserted, with the id assigned by the db (the id passed in is ignored)
     */
    override suspend fun insertTodoItem(todoItem: TodoItem): Either<DomainError, TodoItem> {
        return awaitCustom(dispatcher) {
            query.run {
                try {
                    transactionWithResult {
                        insertItem(todoItem.toDb())
                        if (changes().executeAsOne() > 0) {
                            val id = lastInsertRowId().executeAsOne()
                            select(id).executeAsOneOrNull()?.toDomain()?.let {
                                Either.success(it)
                            } ?: Either.fail(DomainError.RetryLater)
                        } else {
                            Either.fail(DomainError.RetryLater)
                        }
                    }
                } catch (t: Throwable) {
                    Either.fail(DomainError.RetryLater)
                }
            }
        }
    }

    /**
     * returns the items inserted, with the id assigned by the db (the id passed in is ignored)
     */
    override suspend fun insertManyTodoItems(todoItems: List<TodoItem>): Either<DomainError, List<TodoItem>> {
        return awaitCustom(dispatcher) {
            query.run {
                try {
                    transactionWithResult {
                        todoItems.forEach {
                            insertItem(it.toDb())
                        }
                        if (changes().executeAsOne() == todoItems.size.toLong()) {
                            Either.success(
                                selectAllLimit(todoItems.size.toLong()).executeAsList().map {
                                    it.toDomain()
                                }
                            )
                        } else {
                            rollback(Either.fail(DomainError.RetryLater))
                        }
                    }
                } catch (t: Throwable) {
                    Either.fail(DomainError.RetryLater)
                }
            }
        }
    }

    /**
     * returns the item which matches the id, or null if not found
     */
    override suspend fun selectTodoItem(id: Long): Either<DomainError, TodoItem?> {
        return awaitCustom(dispatcher) {
            try {
                query.select(id).executeAsOneOrNull()?.toDomain().let {
                    Either.success(it)
                }
            } catch (t: Throwable) {
                Either.fail(DomainError.RetryLater)
            }
        }
    }

    override suspend fun selectAll(): Either<DomainError, Pair<List<TodoItem>, Long>> {
        return awaitCustom(dispatcher) {
            query.run {
                try {
                    transactionWithResult {
                        val items = selectAll().executeAsList().map {
                            it.toDomain()
                        }
                        val total = count().executeAsOne()
                        Either.success(items to total)
                    }
                } catch (t: Throwable) {
                    Either.fail(DomainError.RetryLater)
                }
            }
        }
    }

    override suspend fun selectAllLimit(limit: Long): Either<DomainError, Pair<List<TodoItem>, Long>> {
        return awaitCustom(dispatcher) {
            query.run {
                try {
                    transactionWithResult {
                        val items = selectAllLimit(limit).executeAsList().map {
                            it.toDomain()
                        }
                        val total = count().executeAsOne()
                        Either.success(items to total)
                    }
                } catch (t: Throwable) {
                    Either.fail(DomainError.RetryLater)
                }
            }
        }
    }

    override suspend fun selectAllDone(done: Boolean): Either<DomainError, Pair<List<TodoItem>, Long>> {
        return awaitCustom(dispatcher) {
            query.run {
                try {
                    transactionWithResult {
                        val items = selectAllDone(done = done).executeAsList().map {
                            it.toDomain()
                        }
                        val total = count().executeAsOne()
                        Either.success(items to total)
                    }
                } catch (t: Throwable) {
                    Either.fail(DomainError.RetryLater)
                }
            }
        }
    }

    /**
     * returns true if a matching item was updated, false if no match was found (based on id)
     */
    override suspend fun updateTodoItem(todoItem: TodoItem): Either<DomainError, Boolean> {
        val todoItemEntity = todoItem.toDb()
        return awaitCustom(dispatcher) {
            query.run {
                try {
                    transactionWithResult {
                        updateItem(
                            creation_timestamp_ms = todoItemEntity.creation_timestamp_ms,
                            label = todoItemEntity.label,
                            done = todoItemEntity.done,
                            id = todoItemEntity.id,
                        )
                        Either.success(query.changes().executeAsOne() == 1L)
                    }
                } catch (t: Throwable) {
                    Either.fail(DomainError.RetryLater)
                }
            }
        }
    }

    /**
     * returns true if a matching item was deleted, false if no match was found
     */
    override suspend fun deleteTodoItem(id: Long): Either<DomainError, Boolean> {
        return awaitCustom(dispatcher) {
            try {
                query.deleteItem(id = id)
                Either.success(query.changes().executeAsOne() == 1L)
            } catch (t: Throwable) {
                Either.fail(DomainError.RetryLater)
            }
        }
    }

    /**
     * Either.success(true) if all items were deleted, success(false) if some of the items were
     * not deleted (perhaps because they were not found)
     */
    override suspend fun deleteMultiple(ids: List<Long>): Either<DomainError, Boolean> {
        return awaitCustom(dispatcher) {
            query.run {
                try {
                    transactionWithResult {
                        ids.forEach {
                            query.deleteItem(id = it)
                        }
                        if (changes().executeAsOne() == ids.size.toLong()) {
                            Either.success(true)
                        } else {
                            Either.success(false)
                        }
                    }
                } catch (t: Throwable) {
                    Either.fail(DomainError.RetryLater)
                }
            }
        }
    }
}
