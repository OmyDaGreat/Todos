package todos.model

import kotlinx.serialization.Serializable

@Serializable
class TodoItem(
    val id: String,
    val todo: String,
)
