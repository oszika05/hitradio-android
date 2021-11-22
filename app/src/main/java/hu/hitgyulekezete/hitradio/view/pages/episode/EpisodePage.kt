package hu.hitgyulekezete.hitradio.view.pages.episode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import com.google.accompanist.flowlayout.FlowRow
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.controller.playPauseForSource
import hu.hitgyulekezete.hitradio.audio.controller.sourcePlaybackState
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.asSource
import hu.hitgyulekezete.hitradio.view.PlayPauseButton
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.EpisodeCard
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.HalfSizeEpisodeCard
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.SmallEpisodeCard
import hu.hitgyulekezete.hitradio.view.components.layout.PageLayout
import hu.hitgyulekezete.hitradio.view.components.person.personcard.PersonCard
import hu.hitgyulekezete.hitradio.view.components.tag.Tag
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import hu.hitgyulekezete.hitradio.view.nowplaying.NowPlayingPadding
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding

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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 24.dp)
                ) {
                    Text(
                        episode.title,
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.primaryText,
                        modifier = Modifier.weight(1f)
                    )

                    val playbackStateFlow =
                        remember(episode.id) { audioController.sourcePlaybackState(episode.id) }
                    val playbackState by playbackStateFlow.collectAsState(AudioStateManager.PlaybackState.STOPPED)

                    PlayPauseButton(
                        circleBackground = true,
                        isLight = true,
                        playbackState = playbackState,
                        modifier = Modifier
                            .padding(start = 14.dp)
                            .size(64.dp)
                    ) {
                        audioController.playPauseForSource(episode.asSource())
                    }
                }

                val description = episode.description ?: episode.program.description
                description?.let { description ->
                    Text(
                        description,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primaryText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                if (episode.tags.isNotEmpty()) {
                    Row(
                        Modifier
                            .padding(top = 16.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Spacer(Modifier.width(8.dp))
                        episode.tags.forEach { tag ->
                            Tag(tag) {
                                onTagClick(tag)
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                }

                Spacer(Modifier.height(24.dp))


                val hostsAndGuests =
                    remember(episode.hosts, episode.guests) { episode.hosts + episode.guests }

                if (hostsAndGuests.isNotEmpty()) {
                    Text(
                        "Résztvevők",
                        color = MaterialTheme.colors.primaryText,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 16.dp)
                    )

                    FlowRow(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        hostsAndGuests.forEach { person ->
                            PersonCard(
                                person = person,
                                onClick = {
                                    onPersonClick(person)
                                },
                                modifier = Modifier.padding(bottom = 24.dp)
                            )
                        }
                    }
                }


                Text(
                    "Adások hasonló témában",
                    color = MaterialTheme.colors.primaryText,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 16.dp)
                )

                val relatedEpisodes by viewModel.related.collectAsState(initial = listOf())

                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(Modifier)

                    relatedEpisodes.forEach { episode ->
                        val playbackState by audioController.sourcePlaybackState(episode.id)
                            .collectAsState(
                                initial = AudioStateManager.PlaybackState.STOPPED
                            )

                        HalfSizeEpisodeCard(
                            episode = episode,
                            playbackState = playbackState,
                            onClick = {
                                onEpisodeClick(episode)
                            },
                            onPlayClick = {
                                audioController.playPauseForSource(episode.asSource())
                            }
                        )
                    }

                    Spacer(Modifier)
                }

                Spacer(Modifier.padding(top = 32.dp))

                NowPlayingPadding()
            }
        }
    }
}