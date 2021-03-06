package hu.hitgyulekezete.hitradio.view.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.hitgyulekezete.hitradio.R
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.controller.playPauseForSource
import hu.hitgyulekezete.hitradio.audio.controller.sourcePlaybackState
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.asSource
import hu.hitgyulekezete.hitradio.view.common.modifiers.coloredshadow.coloredShadow
import hu.hitgyulekezete.hitradio.view.components.LoginButton
import hu.hitgyulekezete.hitradio.view.components.LogoutButton
import hu.hitgyulekezete.hitradio.view.components.button.Button
import hu.hitgyulekezete.hitradio.view.components.button.ButtonVariant
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.EpisodeCard
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.EpisodeCardSkeleton
import hu.hitgyulekezete.hitradio.view.components.login.LocalUser
import hu.hitgyulekezete.hitradio.view.components.news.newscard.NewsCard
import hu.hitgyulekezete.hitradio.view.components.news.newscard.NewsCardSkeleton
import hu.hitgyulekezete.hitradio.view.components.person.personcard.PersonCard
import hu.hitgyulekezete.hitradio.view.components.skeleton.skeleton
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import hu.hitgyulekezete.hitradio.view.nowplaying.NowPlayingPadding
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding
import java.util.*

@Composable
fun HomePage(
    audioController: AudioController,
    viewModel: HomePageViewModel = hiltViewModel(),
    onNavigateToEpisode: (episode: Episode) -> Unit = {},
    onNavigateToEpisodesPage: () -> Unit = {},
    onNavigateToSettingsPage: () -> Unit = {},
    onNavigateToDownloads: () -> Unit = {},
    onNavigateToNewsPage: () -> Unit = {},
    onNavigateToNewsItem: (item: News) -> Unit = {},
    onNavigateToPeoplePage: () -> Unit = {},
    onNavigateToPerson: (person: Person) -> Unit = {},
    onLogin: () -> Unit = {},
) {
    val episodes by viewModel.episodes.collectAsState(initial = listOf())
    val news by viewModel.news.collectAsState(initial = listOf())
    val people by viewModel.people.collectAsState(initial = listOf())

    val loading by viewModel.isLoading.collectAsState(initial = setOf())

    LazyColumn(
        Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 24.dp),
    ) {
        item("header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "F??oldal",
                    color = MaterialTheme.colors.primaryText,
                    style = MaterialTheme.typography.h1,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (LocalUser.current != null) {
                        LogoutButton(onSuccess = onLogin)
                    } else {
                        LoginButton(onSuccess = onLogin)
                    }

//                    IconButton(
//                        modifier = Modifier.size(28.dp),
//                        onClick = onNavigateToDownloads
//                    ) {
//                        Icon(
//                            painterResource(id = R.drawable.ic_hr_download),
//                            contentDescription = "Let??lt??sek"
//                        )
//                    }
//
//                    IconButton(
//                        modifier = Modifier.size(28.dp),
//                        onClick = onNavigateToSettingsPage
//                    ) {
//                        Icon(
//                            painterResource(id = R.drawable.ic_hr_settings),
//                            contentDescription = "Be??ll??t??sok"
//                        )
//                    }
                }
            }
        }

        item("episode_header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Legfrissebb ad??sok",
                    color = MaterialTheme.colors.primaryText,
                    style = MaterialTheme.typography.h2,
                )

                Button(
                    "??sszes",
                    variant = ButtonVariant.Ternary,
                    onClick = onNavigateToEpisodesPage,
                )
            }
        }

        if (loading.contains(HomePageViewModel.Loading.Episodes)) {
            items(2) {
                EpisodeCardSkeleton(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }

        items(episodes) { episode ->
            val playbackState by audioController.sourcePlaybackState(episode.asSource())
                .collectAsState(AudioStateManager.PlaybackState.STOPPED)

            EpisodeCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                episode = episode,
                playbackState = playbackState,
                onPlayClick = {
                    audioController.playPauseForSource(episode.asSource())
                },
                onClick = {
                    onNavigateToEpisode(episode)
                }
            )
        }

        item("news_header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Legfrissebb h??rek",
                    color = MaterialTheme.colors.primaryText,
                    style = MaterialTheme.typography.h2,
                )

                Button(
                    "??sszes",
                    variant = ButtonVariant.Ternary,
                    onClick = onNavigateToNewsPage,
                )
            }
        }

        if (loading.contains(HomePageViewModel.Loading.News)) {
            items(2) {
                NewsCardSkeleton(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }

        items(news) { item ->
            NewsCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                item = item,
                onClick = { onNavigateToNewsItem(item) }
            )
        }

        item("people_header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "N??pszer?? szem??lyek",
                    color = MaterialTheme.colors.primaryText,
                    style = MaterialTheme.typography.h2,
                )

                Button(
                    "??sszes",
                    variant = ButtonVariant.Ternary,
                    onClick = onNavigateToNewsPage,
                )
            }
        }

        item("people") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .coloredShadow()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(horizontal = 16.dp)
                ) {
                    people.forEach { person ->
                        PersonCard(
                            person,
                            onClick = { onNavigateToPerson(person) },
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }

            }
        }

        nowPlayingPadding()
    }
}