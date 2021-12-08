package hu.hitgyulekezete.hitradio.view.components.programguide

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.layout.live
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import java.text.SimpleDateFormat
import java.util.*

private val formatter = SimpleDateFormat("HH:mm")

@Composable
fun ProgramGuideItem(
    title: String,
    description: String? = null,
    time: Date,
    isLive: Boolean = false,
) {
    Row(Modifier.fillMaxWidth()) {
        Text(
            formatter.format(time),
            style = MaterialTheme.typography.h3,
            color = if (isLive) MaterialTheme.colors.live else MaterialTheme.colors.primaryText
        )

        Column(
            Modifier
                .padding(start = 24.dp)
                .fillMaxWidth()
        ) {
            Text(
                title,
                style = MaterialTheme.typography.h3,
                color = if (isLive) MaterialTheme.colors.live else MaterialTheme.colors.primaryText
            )

            description?.let { description ->
                Text(
                    description,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.primaryText,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }
    }
}

@Preview
@Composable
fun Preview_ProgramGuideItem() {
    PreviewContainer() {
        Column() {
            ProgramGuideItem("Test", "test ".repeat(20), Date(), isLive = true)
            Spacer(Modifier.height(16.dp))
            ProgramGuideItem("Test", "test ".repeat(20), Date())
            ProgramGuideItem("Test", time = Date())

        }
    }
}