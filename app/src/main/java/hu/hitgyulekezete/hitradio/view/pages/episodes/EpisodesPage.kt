package hu.hitgyulekezete.hitradio.view.pages.episodes

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import hu.hitgyulekezete.hitradio.audio.controller.*
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.asSource
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.SmallEpisodeCard
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding

@Composable
fun EpisodesPage(
    viewModel: EpisodesPageViewModel = viewModel(),
    audioController: AudioController,
    initialSearch: String = "",
    programId: String? = null,
    onEpisodeClick: (Episode) -> Unit = {}
) {
    val search = viewModel.search.collectAsState(initial = "")
    val episodes = viewModel.episodes.collectAsLazyPagingItems()

    LaunchedEffect(initialSearch, programId) {
        viewModel.reset(
            initialSearch = initialSearch,
            programId = programId
        )
    }

    val mediaId = audioController.mediaId.collectAsState()
    val playbackState = audioController.playbackState.collectAsState()

    LazyColumn {
        item("header") {
            TextField(value = search.value, onValueChange = {
                viewModel.search.compareAndSet(search.value, it)
            })
        }

        if (episodes.loadState.refresh is LoadState.Loading) {
            item(key = "refresh_loading") {
                CircularProgressIndicator()
            }
        } else {
            items(episodes) { episode ->
                if (episode == null) {
                    return@items
                }

                val playbackState by audioController.sourcePlaybackState(episode.id).collectAsState(
                    initial = AudioStateManager.PlaybackState.STOPPED,
                )

                SmallEpisodeCard(
                    episode = episode,
                    playbackState = playbackState,
                    onClick = {
                        onEpisodeClick(episode)
                    },
                    onPlayClick = {
                        audioController.playPauseForSource(episode.asSource())
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                )

//                Row() {
//                    Text(episode.title,
//                        modifier = Modifier.clickable {
//                            onEpisodeClick(episode)
//                        }
//                    )
//
//                    val playbackStateFlow =
//                        remember(episode.id) { audioController.sourcePlaybackState(episode.id) }
//                    val playbackStateForSource by playbackStateFlow.collectAsState(AudioStateManager.PlaybackState.STOPPED)
//
//
//                    Button(onClick = {
//                        audioController.playPauseForSource(episode.asSource())
//                    }) {
//
//                        Text(
//                            if (playbackStateForSource.isPlaying()) {
//                                "stop"
//                            } else {
//                                "play"
//                            }
//                        )
//                    }
//                }

            }
        }

        if (episodes.loadState.append is LoadState.Loading) {
            item(key = "append_loading") {
                CircularProgressIndicator()
            }
        }

        nowPlayingPadding()
    }
}