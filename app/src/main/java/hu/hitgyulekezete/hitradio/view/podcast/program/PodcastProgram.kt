@file:JvmName("PodcastProgramKt")

package hu.hitgyulekezete.hitradio.view.podcast.program

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager


import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram
import hu.hitgyulekezete.hitradio.view.podcast.PodcastProgramListItem

@Composable
fun PodcastProgram(
    podcastProgram: PodcastProgram,
    currentMediaId: String?,
    playbackState: AudioStateManager.PlaybackState,
    getIsDownloaded: (Source) -> Boolean,
    downloadPodcast: (Source) -> Unit,
    onPlay: (Source) -> Unit,
) {
    Column() {
        Text(
            text = podcastProgram.name,
            fontSize = 28.sp,
            modifier = Modifier.padding(all = 8.dp)
        )
        Text(
            text = podcastProgram.description,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(all = 8.dp)
        )

        LazyColumn() {
            items(podcastProgram.podcasts) { podcast ->
                PodcastListItem(
                    podcast = podcast,
                    onPlay = onPlay,
                    getIsDownloaded = getIsDownloaded,
                    downloadPodcast = downloadPodcast,
                    playbackState = if (currentMediaId == podcast.id) {
                        playbackState
                    } else {
                        AudioStateManager.PlaybackState.STOPPED
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PodcastsPreview() {
    var currentMediaId by remember { mutableStateOf<String?>(null) }
    var playbackState by remember { mutableStateOf(AudioStateManager.PlaybackState.STOPPED) }

    PodcastProgram(
        podcastProgram = PodcastProgram.test1,
        onPlay = { source ->
            if (currentMediaId == source.id) {
                playbackState = playbackState.toggle()
            } else {
                currentMediaId = source.id
                playbackState = AudioStateManager.PlaybackState.PLAYING
            }

        },
        currentMediaId = currentMediaId,
        playbackState = playbackState,
        getIsDownloaded = {
            false
        },
        downloadPodcast = {

        }
    )
}