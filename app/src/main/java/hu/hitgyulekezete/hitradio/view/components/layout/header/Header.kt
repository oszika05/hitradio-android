package hu.hitgyulekezete.hitradio.view.components.layout.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.hitgyulekezete.hitradio.view.common.modifiers.coloredshadow.nowPlayingShadow
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.layout.primaryText

@Composable
fun Header(
    title: String,
    isTransparent: Boolean = false,
    isBackButtonEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .nowPlayingShadow()
            .background(
                if (isTransparent) {
                    Color.Transparent
                } else {
                    MaterialTheme.colors.surface
                }
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isBackButtonEnabled) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isTransparent) {
                            MaterialTheme.colors.surface
                        } else {
                            Color.Transparent
                        }
                    )
                    .run {
                        if (isTransparent) {
                            this.nowPlayingShadow()
                        } else {
                            this
                        }
                    }
                    .clickable {
                        onBackClick()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Vissza",
                    modifier = Modifier
                        .size(28.dp)
                )
            }

        }

        Text(
            title,
            color = MaterialTheme.colors.primaryText,
            style = MaterialTheme.typography.h3,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 6.dp)
                .padding(bottom = 2.dp),
        )
    }
}

@Preview
@Composable
fun Preview_Header() {
    PreviewContainer(disablePadding = true) {

        LazyColumn {
            item {
                Header("Test")
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            items(100) {
                Text("test")
            }
        }
    }
}