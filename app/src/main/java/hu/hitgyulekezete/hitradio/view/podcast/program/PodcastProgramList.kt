package hu.hitgyulekezete.hitradio.view.podcast

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hu.hitgyulekezete.hitradio.view.PAGE_PODCAST_PROGRAM
import hu.hitgyulekezete.hitradio.view.makePodcastProgramPageLink

@Composable
fun PodcastProgramList(
    navController: NavHostController,
    programs: List<PodcastProgram>
) {
    LazyColumn(
        Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {
        items(programs) { program ->
            Box(
                Modifier
                    .clickable {
                        navController.navigate(makePodcastProgramPageLink(program.id))
                    }
            ) {
                PodcastProgramListItem(program = program)
            }
        }
    }
}

@Preview
@Composable
fun PodcastProgramListPreview() {
    val navController = rememberNavController()
    PodcastProgramList(
        navController = navController,
        programs = listOf(PodcastProgram.test1, PodcastProgram.test1)
    )
}