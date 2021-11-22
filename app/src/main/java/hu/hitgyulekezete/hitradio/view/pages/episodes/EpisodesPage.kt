package hu.hitgyulekezete.hitradio.view.pages.episodes

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import hu.hitgyulekezete.hitradio.audio.controller.*
import hu.hitgyulekezete.hitradio.model.common.date.daysSince
import hu.hitgyulekezete.hitradio.model.common.date.toReadableString
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.asSource
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.SmallEpisodeCard
import hu.hitgyulekezete.hitradio.view.components.episode.episodecard.SmallEpisodeCardSkeleton
import hu.hitgyulekezete.hitradio.view.components.layout.PageLayout
import hu.hitgyulekezete.hitradio.view.components.list.GroupHeader
import hu.hitgyulekezete.hitradio.view.components.list.GroupHeaderSkeleton
import hu.hitgyulekezete.hitradio.view.components.list.groupedByItems
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding

@ExperimentalFoundationApi
@Composable
fun EpisodesPage(
    viewModel: EpisodesPageViewModel = viewModel(),
    audioController: AudioController,
    initialSearch: String = "",
    programId: String? = null,
    onEpisodeClick: (Episode) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val search by viewModel.search.collectAsState(initial = "")
    val episodes = viewModel.episodes.collectAsLazyPagingItems()

    LaunchedEffect(initialSearch, programId) {
        viewModel.reset(
            initialSearch = initialSearch,
            programId = programId
        )
    }

    val mediaId = audioController.mediaId.collectAsState()
    val playbackState = audioController.playbackState.collectAsState()

    PageLayout(
        headerTitle = "Legfrissebb Adások",
        onBackClick = onBackClick,
    ) {
        item("header") {
            hu.hitgyulekezete.hitradio.view.components.textfield.TextField(
                value = search,
                onValueChange = { viewModel.search.value = it },
                placeholder = "Keresés",
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp)
            )
        }

        groupedByItems(
            items = episodes,
            key = { it.id },
            groupBy = { it.date.daysSince().toReadableString() },
            groupKey = { it },
            header = { group ->
                GroupHeader(group)
            },
            skeleton = { SmallEpisodeCardSkeleton() },
            headerSkeleton = { GroupHeaderSkeleton() }
        ) { episode ->
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
                    .padding(bottom = 16.dp)
            )
        }

        if (episodes.loadState.append is LoadState.Loading) {
            item(key = "append_loading") {
                CircularProgressIndicator()
            }
        }

        nowPlayingPadding()
    }
}