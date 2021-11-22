package hu.hitgyulekezete.hitradio.view.pages.discover

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.controller.playPauseForSource
import hu.hitgyulekezete.hitradio.audio.controller.sourcePlaybackState
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.asSource
import hu.hitgyulekezete.hitradio.view.common.modifiers.coloredshadow.coloredShadow
import hu.hitgyulekezete.hitradio.view.components.button.Button
import hu.hitgyulekezete.hitradio.view.components.button.ButtonVariant
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.EpisodeCard
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.SmallEpisodeCard
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.SmallEpisodeCardSkeleton
import hu.hitgyulekezete.hitradio.view.components.person.personcard.PersonCard
import hu.hitgyulekezete.hitradio.view.components.program.programcard.ProgramCard
import hu.hitgyulekezete.hitradio.view.components.textfield.TextField
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding


@ExperimentalFoundationApi
@Composable
fun DiscoverPage(
    viewModel: DiscoverPageViewModel = hiltViewModel(),
    audioController: AudioController,
    onAllProgramsClick: (search: String) -> Unit = {},
    onProgramClick: (program: Program) -> Unit = {},
    onAllPeopleClick: (search: String) -> Unit = {},
    onPersonClick: (person: Person) -> Unit = {},
    onEpisodeClick: (episode: Episode) -> Unit = {},
) {

    val search by viewModel.search.collectAsState("")

    val episodes = viewModel.episodes.collectAsLazyPagingItems()

    LazyColumn {
        item("header") {
            Spacer(Modifier.height(16.dp))

            TextField(
                value = search,
                onValueChange = {
                    viewModel.search.value = it
                },
                placeholder = "Keresés",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        item("programs") {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Műsorok",
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.primaryText,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                    )
                    Button(
                        text = "Összes",
                        variant = ButtonVariant.Ternary,
                        onClick = {
                            onAllProgramsClick(search)
                        }
                    )
                }


                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    val programs by viewModel.programs.collectAsState(listOf())

                    programs.forEach { program ->
                        ProgramCard(
                            program = program,
                            onClick = { onProgramClick(program) },
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)

                        )
                    }
                }
            }
        }

        item("people") {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Személyek",
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.primaryText,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                    )
                    Button(
                        text = "Összes",
                        variant = ButtonVariant.Ternary,
                        onClick = {
                            onAllPeopleClick(search)
                        }
                    )
                }


                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .coloredShadow()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colors.surface)
                ) {
                    val people by viewModel.people.collectAsState(listOf())

                    people.forEach { person ->
                        PersonCard(
                            person = person,
                            onClick = { onPersonClick(person) },
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        item("episode_header") {
            Text(
                "Adások",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.primaryText,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
        }

        if (episodes.loadState.refresh is LoadState.Loading) {
            items(10) {
                SmallEpisodeCardSkeleton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                )
            }
        }

        items(episodes) { episode ->
            episode ?: return@items

            val playbackStateFlow = audioController.sourcePlaybackState(episode.asSource().id)
            val playbackState by playbackStateFlow.collectAsState(AudioStateManager.PlaybackState.STOPPED)

            SmallEpisodeCard(
                episode = episode,
                playbackState = playbackState,
//                playbackState = AudioStateManager.PlaybackState.STOPPED,
                onClick = {
                    onEpisodeClick(episode)
                },
                onPlayClick = {
                    audioController.playPauseForSource(episode.asSource())
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )
        }

        nowPlayingPadding()
    }
}