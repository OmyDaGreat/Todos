package todos.model

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Todos : UUIDTable() {
    val ownerId = varchar("ownerId", 255)
    val todo = varchar("todo", 255)
}

class TodoStore {
    init {
        Database.connect(
            url = "jdbc:postgresql://db:5432/todos",
            driver = "org.postgresql.Driver",
            user = "malefic",
            password = "password",
        )

        transaction {
            SchemaUtils.create(Todos)
        }
    }

    fun add(
        ownerId: String,
        todoString: String,
    ) {
        transaction {
            Todos.insert {
                it[id] = UUID.randomUUID()
                it[Todos.ownerId] = ownerId
                it[todo] = todoString
            }
        }
    }

    fun remove(
        ownerId: String,
        id: String,
    ) {
        transaction {
            Todos.deleteWhere {
                (Todos.ownerId eq ownerId) and (Todos.id eq UUID.fromString(id))
            }
        }
    }

    operator fun get(ownerId: String): List<TodoItem> =
        transaction {
            Todos
                .select(Todos.ownerId eq ownerId)
                .map {
                    TodoItem(
                        id = it[Todos.id].toString(),
                        todo = it[Todos.todo],
                    )
                }
        }
}
