package todos.styles

import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em

val TitleStyle =
    CssStyle.base {
        Modifier
            .lineHeight(1.15)
            .fontSize(4.cssRem)
            .margin(top = 0.4.em, bottom = 0.6.em)
            .fontWeight(FontWeight.Bold)
    }
