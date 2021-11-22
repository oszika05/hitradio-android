package hu.hitgyulekezete.hitradio.view.pages.episode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.controller.playPauseForSource
import hu.hitgyulekezete.hitradio.audio.controller.sourcePlaybackState
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.asSource
import hu.hitgyulekezete.hitradio.view.PlayPauseButton
import hu.hitgyulekezete.hitradio.view.components.layout.PageLayout
import hu.hitgyulekezete.hitradio.view.components.tag.Tag
import hu.hitgyulekezete.hitradio.view.nowplaying.NowPlayingPadding

@Composable
fun EpisodePage(
    episodeId: String,
    audioController: AudioController,
    onEpisodeClick: (Episode) -> Unit = {},
    onPersonClick: (Person) -> Unit = {},
    onTagClick: (String) -> Unit = {},
    viewModel: EpisodePageViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    LaunchedEffect(episodeId) {
        viewModel.episodeId.value = episodeId
    }

    // TODO loading
    val episode by viewModel.episode.collectAsState(null)

    PageLayout(
        headerTitle = "",
        onBackClick = onBackClick
    ) {
        episode?.let { episode ->
            item(episode.id) {
                Text(episode.title)

                val playbackStateFlow =
                    remember(episode.id) { audioController.sourcePlaybackState(episode.id) }
                val playbackState by playbackStateFlow.collectAsState(AudioStateManager.PlaybackState.STOPPED)
                PlayPauseButton(playbackState = playbackState) {
                    audioController.playPauseForSource(episode.asSource())
                }

                if (episode.description != null) {
                    Text(episode.description!!)
                } else if (episode.program.description != null) {
                    Text(episode.program.description!!)
                }

                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    episode.tags.forEach { tag ->
                        Tag(tag) {
                            onTagClick(tag)
                        }
                    }
                }

                val hostsAndGuests =
                    remember(episode.hosts, episode.guests) { episode.hosts + episode.guests }

                if (hostsAndGuests.isNotEmpty()) {
                    Text("Résztvevők")

                    hostsAndGuests.forEach { person ->
                        Text(
                            person.name,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    onPersonClick(person)
                                }
                        )
                    }
                }

                Text("Hasonló adások")

                val relatedEpisodes by viewModel.related.collectAsState(initial = listOf())

                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    relatedEpisodes.forEach { episode ->
                        Text(
                            episode.title,
                            Modifier
                                .clickable {
                                    onEpisodeClick(episode)
                                }
                        )
                    }
                }

                NowPlayingPadding()
            }
        }
    }
}