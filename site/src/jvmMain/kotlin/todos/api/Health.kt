package todos.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.http.HttpMethod
import com.varabyte.kobweb.api.http.setBodyText

/**
 * Health check endpoint to verify the server is running.
 */
@Api
fun health(ctx: ApiContext) {
    if (ctx.req.method != HttpMethod.GET) return
    ctx.res.setBodyText("OK")
}
