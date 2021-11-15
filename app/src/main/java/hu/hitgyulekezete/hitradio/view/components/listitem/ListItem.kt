package hu.hitgyulekezete.hitradio.view.components.listitem

import android.media.session.PlaybackState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
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
fun ListItem(
    image: Painter,
    contentDescription: String? = null,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {},
    playbackState: AudioStateManager.PlaybackState? = null,
    onPlayPauseClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .coloredShadow()
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.77f)
        ) {
            Image(
                painter = image,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

            playbackState?.let { playbackState ->
                PlayPauseButton(
                    modifier = Modifier
                        .width(64.dp)
                        .height(64.dp),
                    playbackState = playbackState
                ) {
                    onPlayPauseClick()
                }
            }
        }


        Text(
            title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.primaryText,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp)
        )

        subtitle?.let { subtitle ->
            Text(
                subtitle.toUpperCase(Locale.current),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.secondaryText,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun Preview_ListIem() {
    PreviewContainer {
        Column {
            ListItem(
                image = rememberImagePainter("https://upload.wikimedia.org/wikipedia/commons/5/51/450px-Shoebill-cropped.JPG"),
                title = "Lorem ipsum dolor sit amet, consect adipiscing elit ut aliquam",
                subtitle = "Hitköznapok",
                playbackState = AudioStateManager.PlaybackState.STOPPED,
            )

            Spacer(Modifier.height(16.dp))

            ListItem(
                image = rememberImagePainter("https://upload.wikimedia.org/wikipedia/commons/5/51/450px-Shoebill-cropped.JPG"),
                title = "Lorem ipsum dolor sit amet, consect adipiscing elit ut aliquam",
            )
        }

    }
}