package foo.bar.clean.data.db.service.todo

import foo.bar.clean.data.db.todo.TodoItemEntity
import foo.bar.clean.domain.services.db.TodoItem

fun TodoItem.toDb(): TodoItemEntity {
    return TodoItemEntity(
        creation_timestamp_ms = createdTimestampMs,
        label = label,
        done = done,
        id = id,
    )
}

fun TodoItemEntity.toDomain(): TodoItem {
    return TodoItem(
        createdTimestampMs = creation_timestamp_ms,
        label = label,
        done = done,
        id = id
    )
}
