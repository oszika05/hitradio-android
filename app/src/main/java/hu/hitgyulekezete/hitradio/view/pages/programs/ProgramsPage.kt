package hu.hitgyulekezete.hitradio.view.pages.programs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.view.components.layout.PageLayout
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding

@Composable
fun ProgramsPage(
    search: String? = null,
    viewModel: ProgramsPageViewModel = hiltViewModel(),
    onProgramClick: (Program) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    LaunchedEffect(search) {
        viewModel.search.value = search
    }

    val programs = viewModel.programs.collectAsLazyPagingItems()

    PageLayout(
        headerTitle = "Műsorok",
        onBackClick = onBackClick,
    ) {
        items(programs) { program ->
            program ?: return@items

            Text(
                program.name,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onProgramClick(program)
                    }
            )
        }

        nowPlayingPadding()
    }
}