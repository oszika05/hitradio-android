package hu.hitgyulekezete.hitradio.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.DownloadManager
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.programguide.api.ProgramGuideApi
import hu.hitgyulekezete.hitradio.view.pages.HomePage
import hu.hitgyulekezete.hitradio.view.pages.NotFoundPage
import hu.hitgyulekezete.hitradio.view.pages.PodcastProgramPage

val podcastProgramIdPath = "podcastProgramId"

val PAGE_HOME = "home"
val PAGE_PODCAST_PROGRAM = "podcast_program/{${podcastProgramIdPath}}"
fun makePodcastProgramPageLink(id: String): String = PAGE_PODCAST_PROGRAM.replace("{${podcastProgramIdPath}}", id)

@Composable
fun Pages(
    navController: NavHostController,
    audioController: AudioController,
    programApi: ProgramGuideApi,
    programRepository: CurrentProgramRepository,
    downloadManager: DownloadManager,
) {
    NavHost(navController = navController, startDestination = PAGE_HOME) {
        composable(PAGE_HOME) {
            HomePage(
                navController,
                programApi = programApi,
                programRepository = programRepository,
                audioController = audioController
            )
        }
        composable(PAGE_PODCAST_PROGRAM) { backStackEntry ->
            if (backStackEntry.arguments?.getString(podcastProgramIdPath) == null) {
                NotFoundPage()
            } else {
                PodcastProgramPage(
                    podcastProgramId = backStackEntry.arguments?.getString(podcastProgramIdPath)!!,
                    audioController = audioController,
                    downloadManager = downloadManager,
                )
            }
        }
    }
}