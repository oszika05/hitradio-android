package hu.hitgyulekezete.hitradio.view.podcast.program

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import hu.hitgyulekezete.hitradio.audio.AudioController
import hu.hitgyulekezete.hitradio.audio.metadata.artUriOrDefault
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.model.podcast.PodcastRepository
import hu.hitgyulekezete.hitradio.view.PlayPauseButton

@Composable
fun PodcastListItem(
    podcast: Source,
    playbackState: AudioController.PlaybackState,
    getIsDownloaded: (Source) -> Boolean,
    downloadPodcast: (Source) -> Unit,
    onPlay: (Source) -> Unit
) {
    var isDownloaded by remember { mutableStateOf(false) }

    LaunchedEffect(podcast) {
        isDownloaded = getIsDownloaded(podcast)
    }

    Row(
        Modifier
            .clickable {
                onPlay(podcast)
            }
    ) {
        Image(
            painter = rememberCoilPainter(
                request = podcast.metadata.artUriOrDefault()
            ),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .width(48.dp)
                .aspectRatio(1f)
                .clip(
                    RoundedCornerShape(4.dp)
                )
        )
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = podcast.name,
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (podcast.description != null) {
                Text(
                    text = podcast.description!!
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 8.dp)
                    .height(48.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(
                    onClick = {
                        downloadPodcast(podcast)
                        isDownloaded = true
                    },
                    enabled = !isDownloaded
                ) {
                    if (!isDownloaded) {
                        Icon(Icons.Default.CloudDownload, contentDescription = "letöltés")
                    } else {
                        Icon(Icons.Default.CloudDone, contentDescription = "letöltve")
                    }
                }

                PlayPauseButton(playbackState = playbackState, onClick = {
                    onPlay(podcast)
                })
            }
        }

    }
}

@Preview
@Composable
fun PodcastListItemPreview() {
    var playbackState by remember { mutableStateOf(AudioController.PlaybackState.STOPPED) }
    PodcastListItem(
        onPlay = {
            playbackState = playbackState.toggle()
        },
        podcast = hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram.test1.podcasts[0],
        playbackState = playbackState,
        getIsDownloaded = {
            false
        },
        downloadPodcast = {

        }
    )
}