package hu.hitgyulekezete.hitradio.view.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.artUriOrDefault
import hu.hitgyulekezete.hitradio.view.PlayPauseButton

@Composable
fun NowPlayingBarCollapsed(
    height: Dp,
    metadata: Metadata,
    playbackState: AudioStateManager.PlaybackState,
    onPlayPausePressed: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Image(
            painter = rememberImagePainter(metadata.artUriOrDefault()),
            contentScale = ContentScale.Crop,
            contentDescription = "album image",
            modifier = Modifier
                .aspectRatio(1f)
                .padding(all = 4.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                )
        )

        Column(
            Modifier
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                metadata.title,
                Modifier
                    .padding(top = 3.dp),
                style = MaterialTheme.typography.h6
            )
            if (metadata.subtitle != null) {
                Text(
                    metadata.subtitle,
                    Modifier.padding(top = 1.dp),
                    style = MaterialTheme.typography.subtitle1
                )
            }

        }


        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            PlayPauseButton(playbackState = playbackState, onClick = onPlayPausePressed)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNowPlayingBarCollapsed() {

    var playbackState by remember { mutableStateOf(AudioStateManager.PlaybackState.STOPPED) }

    NowPlayingBarCollapsed(
        height = 64.dp,
        playbackState = playbackState,
        metadata = Metadata(
            title = "Title",
            subtitle = "Subtitle",
            artUri = null,
            type = MetadataType.LIVE
        ),
        onPlayPausePressed = {
            playbackState = playbackState.toggle()
        }
    )
}