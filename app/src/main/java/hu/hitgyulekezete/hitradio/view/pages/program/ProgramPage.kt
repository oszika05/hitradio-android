package hu.hitgyulekezete.hitradio.view.pages.program

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.controller.playPauseForSource
import hu.hitgyulekezete.hitradio.audio.controller.sourcePlaybackState
import hu.hitgyulekezete.hitradio.model.common.date.daysSince
import hu.hitgyulekezete.hitradio.model.common.date.toReadableString
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.asSource
import hu.hitgyulekezete.hitradio.view.PlayPauseButton
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.SmallEpisodeCard
import hu.hitgyulekezete.hitradio.view.components.layout.PageLayout
import hu.hitgyulekezete.hitradio.view.components.list.GroupHeader
import hu.hitgyulekezete.hitradio.view.components.list.groupedByItems
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding

@ExperimentalFoundationApi
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

    val program by viewModel.program.collectAsState(initial = null)

    val image = if (program != null) {
        rememberImagePainter(program?.pictureOrDefault)
    } else {
        null
    }


    PageLayout(
        headerTitle = program?.let { it.name } ?: "",
        headerImage = image,
        onBackClick = onBackClick,
    ) {
        item("header") {

            program?.let { program ->
//                Image(
//                    painter = rememberImagePainter(program.pictureOrDefault),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(1.77f)
//                )

                Text(
                    program.name,
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.primaryText,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(top = 8.dp)
                )

                program.description?.let { description ->
                    Text(description)
                }
            }
        }

        groupedByItems(
            episodes,
            key = { it.id },
            groupBy = { it.date.daysSince().toReadableString() },
            groupKey = { it },
            header = { group ->
                GroupHeader(
                    group = group,
                    autoPadding = false,
                    noBackground = true,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        ) { episode ->
            val playbackStateFlow =
                remember(episode.id) { audioController.sourcePlaybackState(episode.id) }
            val playbackState by playbackStateFlow.collectAsState(initial = AudioStateManager.PlaybackState.STOPPED)

            SmallEpisodeCard(
                episode = episode,
                playbackState = playbackState,
                onPlayClick = {
                    audioController.playPauseForSource(episode.asSource())
                },
                onClick = {
                    onEpisodeClick(episode)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )
        }

        item("bottom_spacing") {
            Spacer(Modifier.padding(top = 40.dp).fillMaxWidth())
            Spacer(Modifier.padding(top = 1000.dp).fillMaxWidth())
        }

        nowPlayingPadding()
    }


}