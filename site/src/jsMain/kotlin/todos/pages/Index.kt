package todos.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.toModifier
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Footer
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import todos.components.widgets.LoadingSpinner
import todos.components.widgets.TodoCard
import todos.components.widgets.TodoForm
import todos.model.TodoItem
import todos.styles.TitleStyle

private suspend fun loadAndReplaceTodos(
    id: String,
    todos: SnapshotStateList<TodoItem>,
) = window.api.get("list?owner=$id").let { listBytes ->
    Snapshot.withMutableSnapshot {
        todos.clear()
        todos.addAll(Json.decodeFromString(listBytes.decodeToString()))
    }
}

@Page
@Composable
fun HomePage() {
    var id by remember { mutableStateOf("") }
    var ready by remember { mutableStateOf(false) }
    var loadingCount by remember { mutableStateOf(1) } // How many API requests are occurring at the same time
    val todos = remember { mutableStateListOf<TodoItem>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        check(!ready && loadingCount == 1)
        id = window.localStorage.getItem("id") ?: run {
            window.api.get("id").decodeToString().also {
                window.localStorage.setItem("id", it)
            }
        }

        loadAndReplaceTodos(id, todos)
        loadingCount--
        ready = true
    }

    Column(
        modifier = Modifier.fillMaxSize().minWidth(600.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        if (!ready) {
            Box(Modifier.fillMaxWidth().margin(top = 2.cssRem), contentAlignment = Alignment.TopCenter) {
                LoadingSpinner()
            }
            return@Column
        }

        Span(
            TitleStyle
                .toModifier()
                .whiteSpace(WhiteSpace.PreWrap)
                .textAlign(TextAlign.Center)
                .toAttrs(),
        ) {
            Text("TODO App with ")
            Link("https://github.com/varabyte/kobweb", "Kobweb")
            Text("!")
        }

        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxWidth().maxWidth(800.px).align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TodoForm("Type a TODO and press ENTER", loadingCount > 0) { todo ->
                    coroutineScope.launch {
                        loadingCount++
                        window.api.post("add?owner=$id&todos=$todo")
                        loadAndReplaceTodos(id, todos)
                        loadingCount--
                    }
                }

                todos.forEachIndexed { i, todo ->
                    TodoCard(onClick = {
                        coroutineScope.launch {
                            loadingCount++
                            todos.removeAt(i)
                            window.api.post("remove?owner=$id&todos=${todo.id}")
                            loadAndReplaceTodos(id, todos)
                            loadingCount--
                        }
                    }) {
                        Text(todo.todo)
                    }
                }
            }

            Spacer()
            Footer {
                Row {
                    SpanText("Project based off of ")
                    Link(
                        "https://github.com/varabyte/kobweb-templates/tree/main/examples/todo",
                        "Varabyte's Kobweb Todo Example",
                    )
                }
            }
        }
    }
}
