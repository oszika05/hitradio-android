package hu.hitgyulekezete.hitradio.view.pages.discover

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.Program


@ExperimentalFoundationApi
@Composable
fun DiscoverPage(
    viewModel: DiscoverPageViewModel = hiltViewModel(),
    onAllProgramsClick: (search: String) -> Unit = {},
    onProgramClick: (program: Program) -> Unit = {},
    onAllPeopleClick: (search: String) -> Unit = {},
    onPersonClick: (person: Person) -> Unit = {},
    onEpisodeClick: (episode: Episode) -> Unit = {},
) {

    val search by viewModel.search.collectAsState("")

    val episodes = viewModel.episodes.collectAsLazyPagingItems()

    LazyColumn {
        item("header_title") {
            Text("Keresés")
        }
        stickyHeader {
            TextField(value = search, onValueChange = {
                viewModel.search.compareAndSet(search, it)
            })
        }

        item("programs") {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Műsorok")
                    Text(
                        "Összes",
                        modifier = Modifier.clickable {
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
                        Text(
                            program.name,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    onProgramClick(program)
                                }
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
                    Text("Személyek")
                    Text(
                        "Összes",
                        modifier = Modifier.clickable {
                            onAllPeopleClick(search)
                        }
                    )
                }


                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    val people by viewModel.people.collectAsState(listOf())

                    people.forEach { person ->
                        Text(
                            person.name,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    onPersonClick(person)
                                }
                        )
                    }
                }
            }
        }

        item("episode_header") {
            Spacer(modifier = Modifier.height(250.dp))
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
    }
}