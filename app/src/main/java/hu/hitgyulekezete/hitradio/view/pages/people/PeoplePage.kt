package hu.hitgyulekezete.hitradio.view.pages.people

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import hu.hitgyulekezete.hitradio.model.program.Person

@ExperimentalFoundationApi
@Composable
fun PeoplePage(
    search: String? = null,
    viewModel: PeoplePageViewModel = hiltViewModel(),
    onPersonClick: (Person) -> Unit = {}
) {
    LaunchedEffect(search) {
        viewModel.search.value = search
    }

    val hosts = viewModel.hosts.collectAsLazyPagingItems()
    val guests = viewModel.guests.collectAsLazyPagingItems()

    val hostsScrollState = rememberLazyListState()
    val guestsScrollState = rememberLazyListState()

    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    Column {
        Text("Személyek")

        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(
                selected = selectedTabIndex == 0,
                text = { Text("Vendégek") },
                onClick = {
                    selectedTabIndex = 0
                }
            )
            Tab(
                selected = selectedTabIndex == 1,
                text = { Text("Műsorvezetők") },
                onClick = {
                    selectedTabIndex = 1
                }
            )
        }

        Crossfade(targetState = selectedTabIndex) { index ->
            when (index) {
                0 -> {
                    LazyColumn(state = guestsScrollState) {
                        items(guests) { guest ->
                            guest ?: return@items

                            Text(
                                guest.name,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        onPersonClick(guest)
                                    }
                            )
                        }
                    }
                }
                1 -> {
                    LazyColumn(state = hostsScrollState) {
                        items(hosts) { host ->
                            host ?: return@items

                            Text(
                                host.name,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        onPersonClick(host)
                                    }
                            )
                        }
                    }
                }
            }
        }
        if (selectedTabIndex == 1) {

        }

    }

}