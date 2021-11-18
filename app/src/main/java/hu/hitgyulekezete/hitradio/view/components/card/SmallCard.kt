package hu.hitgyulekezete.hitradio.view.components.card

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.view.PlayPauseButton
import hu.hitgyulekezete.hitradio.view.common.modifiers.coloredshadow.coloredShadow
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import hu.hitgyulekezete.hitradio.view.layout.secondaryText

@Composable
fun SmallCard(
    title: String,
    subtitle: String? = null,
    image: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit = {},
    playbackState: AudioStateManager.PlaybackState? = null,
    onPlayPauseClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .coloredShadow()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(start = 16.dp, end = 8.dp)
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))

        ) {
            Image(
                painter = image,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )

            playbackState?.let { playbackState ->
                PlayPauseButton(
                    modifier = Modifier
                        .size(64.dp),
                    isLight = true,
                    circleAroundButton = false,
                    playbackState = playbackState,
                ) {
                    onPlayPauseClick()
                }
            }
        }

        Column(
            Modifier
                .padding(end = 16.dp)
                .padding(vertical = 8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primaryText,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            subtitle?.let { subtitle ->
                Text(
                    subtitle,
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondaryText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_SmallCard() {
    PreviewContainer {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            SmallCard(
                image = rememberImagePainter("https://upload.wikimedia.org/wikipedia/commons/5/51/450px-Shoebill-cropped.JPG"),
                title = "Lorem ipsum dolor sit amet, consect adipiscing elit ut aliquam",
            )

            Spacer(Modifier.height(16.dp))

            SmallCard(
                image = rememberImagePainter("https://upload.wikimedia.org/wikipedia/commons/5/51/450px-Shoebill-cropped.JPG"),
                title = "Lorem ipsum dolor sit amet, consect adipiscing elit ut aliquam",
                subtitle = "Középpont"
            )

            Spacer(Modifier.height(16.dp))

            SmallCard(
                image = rememberImagePainter("https://upload.wikimedia.org/wikipedia/commons/5/51/450px-Shoebill-cropped.JPG"),
                title = "Lorem ipsum dolor sit amet, consect adipiscing elit ut aliquam aliquam aliquam aliquam aliquam",
                subtitle = "Középpont"
            )

            Spacer(Modifier.height(16.dp))

            SmallCard(
                image = rememberImagePainter("https://upload.wikimedia.org/wikipedia/commons/5/51/450px-Shoebill-cropped.JPG"),
                title = "Lorem ipsum dolor sit amet, consect adipiscing elit ut aliquam aliquam aliquam aliquam aliquam",
                subtitle = "Középpont",
                playbackState = AudioStateManager.PlaybackState.STOPPED
            )
        }

    }
}