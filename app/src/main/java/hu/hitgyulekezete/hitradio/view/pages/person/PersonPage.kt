package hu.hitgyulekezete.hitradio.view.pages.person

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.pictureOrDefault
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding

@Composable
fun PersonPage(
    personId: String,
    viewModel: PersonPageViewModel = hiltViewModel(),
    onEpisodeClick: (Episode) -> Unit = {}
) {
    LaunchedEffect(personId) {
        viewModel.personId.value = personId
    }

    val person by viewModel.person.collectAsState(null)
    val episodes = viewModel.episodes.collectAsLazyPagingItems()

    person?.let { person ->
        LazyColumn {
            item("header") {
                Image(
                    rememberImagePainter(person.pictureOrDefault),
                    contentDescription = person.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )

                Text(person.name, style = MaterialTheme.typography.h4)

                person.description?.let { description ->
                    Text(description)
                }

                Text("Adások")
            }

            items(episodes) { episode ->
                episode ?: return@items

                Text(
                    episode.title,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onEpisodeClick(episode)
                        }
                )
            }

            nowPlayingPadding()
        }
    }
}