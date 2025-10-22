package todos.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.HttpMethod
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import todos.model.TodoStore

/**
 * API endpoint to list all todos items for a specific owner.
 *
 * This function is annotated with `@Api` to indicate that it is an API endpoint.
 * It processes the request context to extract the owner parameter,
 * retrieves the todos items from the `TodoStore`, and sets the response body
 * with the serialized list of todos items.
 *
 * @param ctx The `ApiContext` containing the request and response information.
 */
@Api
fun listTodos(ctx: ApiContext) {
    // Check if the request method is GET, if not, return early
    if (ctx.req.method != HttpMethod.GET) return

    // Extract owner parameter from the request, if null, return early
    val ownerId = ctx.req.params["owner"] ?: return

    // Set the response body with the serialized list of todos items for the owner
    ctx.res.setBodyText(Json.encodeToString(TodoStore[ownerId]))
}
