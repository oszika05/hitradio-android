package hu.hitgyulekezete.hitradio.view.components.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.chip.Chip
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.layout.secondaryText
import hu.hitgyulekezete.hitradio.view.layout.tag

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .height(30.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.tag)
            .run {
                if (onClick != null) {
                    return@run this
                        .clickable { onClick() }
                }

                this
            }
    ) {
        Text(
            text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondaryText,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp),
        )
    }
}

@Preview
@Composable
fun Preview_Tag() {
    PreviewContainer {
        Row {
            Tag("Test1")

            Spacer(modifier = Modifier.width(8.dp))

            Tag("Test2")

            Spacer(modifier = Modifier.width(8.dp))

            Tag("Test2") {

            }

        }
    }
}