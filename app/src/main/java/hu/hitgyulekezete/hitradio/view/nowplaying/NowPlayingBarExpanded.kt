package hu.hitgyulekezete.hitradio.view.nowplaying

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Horizontal
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.artUriOrDefault
import hu.hitgyulekezete.hitradio.view.PlayPauseButton

@Composable
fun NowPlayingBarExpanded(
    playbackState: AudioStateManager.PlaybackState,
    metadata: Metadata,
    seekPercentage: Float?,
    volumePercentage: Float,
    onPlayPause: () -> Unit,
    onSeekTo: (Float) -> Unit,
    onSetVolume: (Float) -> Unit,
    onClose: () -> Unit,
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            Modifier
                .padding(all = 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                onClose()
            }) {
                Icon(Icons.Default.KeyboardArrowDown, "close drawer")
            }
        }
        Row(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberImagePainter(metadata.artUriOrDefault()),
                contentScale = ContentScale.Crop,
                contentDescription = "album image",
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(all = 16.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
            )
        }
        Row(
            Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            seekPercentage?.let { seekPercentage ->
                Slider(value = seekPercentage, onValueChange = { onSeekTo(it) })
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    metadata.title,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (metadata.subtitle != null) {
                    Text(
                        metadata.subtitle,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Row(
            Modifier
                .height(80.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            PlayPauseButton(playbackState = playbackState, onClick = onPlayPause)
        }
        Row(
            Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                onSetVolume((volumePercentage - 0.1f).coerceAtLeast(0f))
            }) {
                Icon(Icons.Default.VolumeDown, "halkítás")
            }
            Slider(
                value = volumePercentage,
                onValueChange = { onSetVolume(it) },
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = {
                onSetVolume((volumePercentage + 0.1f).coerceAtMost(1f))
            }) {
                Icon(Icons.Default.VolumeUp, "halkítás")
            }
        }
    }
}

@Preview
@Composable
fun PreviewNowPlayingBarExpanded() {
    var playbackState by remember { mutableStateOf(AudioStateManager.PlaybackState.STOPPED) }
    var seekPercentage by remember { mutableStateOf(0.0f) }
    var volumePercentage by remember { mutableStateOf(0.0f) }


    NowPlayingBarExpanded(
        playbackState = playbackState,
        metadata = Metadata(
            title = "Teszt",
            subtitle = "Teszt teszt",
            artUri = "https://myonlineradio.hu/public/uploads/radio_img/hit-radio/play_250_250.jpg",
            type = MetadataType.LIVE,
        ),
        seekPercentage = seekPercentage,
        volumePercentage = volumePercentage,
        onPlayPause = {
            playbackState = playbackState.toggle()
        },
        onSeekTo = { where ->
            seekPercentage = where
        },
        onSetVolume = { newVolume ->
            volumePercentage = newVolume
        },
        onClose = {

        }
    )
}