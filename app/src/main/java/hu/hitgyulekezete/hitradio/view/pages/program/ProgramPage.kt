package hu.hitgyulekezete.hitradio.view.pages.program

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.controller.playPauseForSource
import hu.hitgyulekezete.hitradio.audio.controller.sourcePlaybackState
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.asSource
import hu.hitgyulekezete.hitradio.view.PlayPauseButton
import hu.hitgyulekezete.hitradio.view.components.layout.PageLayout
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding

@Composable
fun ProgramPage(
    programId: String,
    audioController: AudioController,
    onEpisodeClick: (Episode) -> Unit = {},
    viewModel: ProgramPageViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    LaunchedEffect(programId) {
        viewModel.programId.value = programId
    }

    val episodes = viewModel.episodes.collectAsLazyPagingItems()

    PageLayout(
        headerTitle = "",
        onBackClick = onBackClick,
    ) {
        item("header") {
            val program by viewModel.program.collectAsState(initial = null)

            program?.let { program ->
                Image(
                    painter = rememberImagePainter(program.pictureOrDefault),
                    contentDescription = program.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )

                Text(program.name)

                program.description?.let { description ->
                    Text(description)
                }
            }
        }


        items(episodes) { episode ->
            episode ?: return@items

            val playbackStateFlow =
                remember(episode.id) { audioController.sourcePlaybackState(episode.id) }
            val playbackState by playbackStateFlow.collectAsState(initial = AudioStateManager.PlaybackState.STOPPED)

            Row {
                Text(
                    text = episode.title,
                    modifier = Modifier
                        .clickable {
                            onEpisodeClick(episode)
                        }
                )

                PlayPauseButton(playbackState = playbackState) {
                    audioController.playPauseForSource(episode.asSource())
                }
            }
        }

        nowPlayingPadding()
    }


}