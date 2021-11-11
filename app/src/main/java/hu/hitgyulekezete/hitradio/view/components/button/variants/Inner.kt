package hu.hitgyulekezete.hitradio.view.components.button.variants

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
internal fun Inner(
    text: String,
    leftIcon: Painter? = null,
    rightIcon: Painter? = null,
) {
    leftIcon?.let { leftIcon ->
        Icon(
            leftIcon,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
        )
    }

    Text(text)

    rightIcon?.let { rightIcon ->
        Icon(
            rightIcon,
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}