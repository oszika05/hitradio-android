package hu.hitgyulekezete.hitradio.view.components.label

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun Label(
    text: String,
    color: Color = MaterialTheme.colors.onBackground,
    style: TextStyle = MaterialTheme.typography.caption,
) {
    Text(
        text,
        color = color,
        style = style,
    )
}