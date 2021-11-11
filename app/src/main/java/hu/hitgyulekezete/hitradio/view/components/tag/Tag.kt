package hu.hitgyulekezete.hitradio.view.components.tag

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.chip.Chip
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Card(
        shape = RoundedCornerShape(50),
        modifier = modifier.run {
            if (onClick != null) {
                return@run this
                    .clickable { onClick() }
            }

            Modifier
        }
    ) {
        Text(
            text,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp),
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