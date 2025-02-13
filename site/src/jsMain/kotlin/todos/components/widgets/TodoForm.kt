package todos.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.silk.style.toAttrs
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.attributes.onSubmit
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.Input

@Composable
fun TodoForm(
    placeholder: String,
    loading: Boolean,
    submitTodo: (String) -> Unit,
) {
    if (loading) {
        TodoCard {
            LoadingSpinner()
        }
    } else {
        var todo by remember { mutableStateOf("") }
        Form(
            attrs =
                listOf(TodoStyle, TodoContainerStyle).toAttrs {
                    onSubmit { evt ->
                        evt.preventDefault()
                        submitTodo(todo)
                    }
                },
        ) {
            Input(
                InputType.Text,
                attrs =
                    listOf(TodoStyle, TodoTextStyle, TodoInputStyle)
                        .toAttrs {
                            placeholder(placeholder)
                            name("todos")
                            onChange { todo = it.value }
                        },
            )
        }
    }
}
