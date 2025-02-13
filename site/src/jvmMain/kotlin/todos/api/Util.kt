package todos.api

import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.http.HttpMethod

/**
 * Extension function for `ApiContext` to process a function with owner and todos parameters.
 *
 * @param function A function that takes a `Pair` of `String` values representing the owner and todos.
 */
fun ApiContext.process(function: ApiContext.(Pair<String, String>) -> Unit) {
    // Check if the request method is POST, if not, return early
    if (req.method != HttpMethod.POST) return

    // Extract owner and todos parameters from the request
    val ownerId = req.params["owner"]
    val todo = req.params["todos"]

    // If either owner or todos is null, return early
    if (ownerId == null || todo == null) {
        return
    }

    // Call the provided function with the owner and todos as a Pair
    function(ownerId to todo)
}
