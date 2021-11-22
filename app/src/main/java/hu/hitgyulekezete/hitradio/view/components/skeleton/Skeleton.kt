package hu.hitgyulekezete.hitradio.view.components.skeleton

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.layout.secondaryText
import hu.hitgyulekezete.hitradio.view.layout.shadow

@Composable
fun Modifier.skeleton(
    visible: Boolean = true,
): Modifier {
    val color = MaterialTheme.colors.shadow.copy(
        red = (MaterialTheme.colors.shadow.red * 1.2f).coerceAtMost(1f),
        green = (MaterialTheme.colors.shadow.green * 1.2f).coerceAtMost(1f),
        blue = (MaterialTheme.colors.shadow.blue * 1.2f).coerceAtMost(1f),
    )

    val highlightColor = MaterialTheme.colors.shadow.copy(
        red = (MaterialTheme.colors.shadow.red * 1.5f).coerceAtMost(1f),
        green = (MaterialTheme.colors.shadow.green * 1.5f).coerceAtMost(1f),
        blue = (MaterialTheme.colors.shadow.blue * 1.5f).coerceAtMost(1f),
    )

    return this.placeholder(
        visible = visible,
        color = color,
        shape = RoundedCornerShape(16.dp),
        highlight = PlaceholderHighlight.fade(
            highlightColor = highlightColor,
        ),
    )
}

@Preview
@Composable
fun Preview_Skeleton() {
    PreviewContainer() {
        LazyColumn {
            items(100) { i ->
                Text(
                    "This is a test text $i",
                    modifier = Modifier.skeleton(visible = i % 2 == 0)
                )
            }
        }
    }
}