package todos.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import todos.model.TodoStore

/**
 * API endpoint to add a new todos item.
 *
 * This function is annotated with `@Api` to indicate that it is an API endpoint.
 * It processes the request context to extract the owner and todos parameters,
 * and adds the todos item to the `TodoStore`.
 *
 * @param ctx The `ApiContext` containing the request and response information.
 */
@Api
fun addTodo(ctx: ApiContext) =
    ctx.process { (ownerId, todo) ->
        // Add the todos item to the TodoStore
        TodoStore.add(ownerId, todo)
        // Set the response status to 200 (OK)
        res.status = 200
    }
