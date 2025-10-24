package todos.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import todos.model.TodoStore

/**
 * API endpoint to remove a todos item.
 *
 * This function is annotated with `@Api` to indicate that it is an API endpoint.
 * It processes the request context to extract the owner and todoId parameters,
 * and removes the todos item from the `TodoStore`.
 *
 * @param ctx The `ApiContext` containing the request and response information.
 */
@Api
fun removeTodo(ctx: ApiContext) =
    ctx.process { (ownerId, todoId) ->
        // Remove the todos item from the TodoStore
        TodoStore.remove(ownerId, todoId)
        // Set the response status to 200 (OK)
        res.status = 200
    }
