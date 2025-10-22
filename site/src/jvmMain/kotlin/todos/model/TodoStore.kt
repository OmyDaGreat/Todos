package todos.model

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Todos : UUIDTable() {
    val ownerId = varchar("ownerId", 255)
    val todo = varchar("todo", 255)
}

object TodoStore {
    init {
        val dbHost = System.getenv("DB_HOST") ?: "db"
        val dbPort = System.getenv("DB_PORT") ?: "5432"
        val dbName = System.getenv("DB_NAME") ?: "todos"
        val dbUser = System.getenv("DB_USER") ?: "malefic"
        val dbPassword = System.getenv("DB_PASSWORD") ?: "password"

        Database.connect(
            url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword,
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
                .selectAll()
                .where { Todos.ownerId eq ownerId }
                .map {
                    TodoItem(
                        id = it[Todos.id].toString(),
                        todo = it[Todos.todo],
                    )
                }
        }
}
