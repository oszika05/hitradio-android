package hu.hitgyulekezete.hitradio.view.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.metadata.source.ChangingMetadataSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService.Companion.LIVE_HITRADIO_ID
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram
import hu.hitgyulekezete.hitradio.model.podcast.repository.MockPodcastRepository
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.current.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.program.api.ProgramApi
import hu.hitgyulekezete.hitradio.model.program.repository.MockProgramRepository
import hu.hitgyulekezete.hitradio.view.OnlineRadio
import hu.hitgyulekezete.hitradio.view.podcast.PodcastProgramList
import java.util.*

@Composable
fun HomePage(
    navController: NavHostController,
    programApi: ProgramApi,
    audioController: AudioController,
    programRepository: CurrentProgramRepository,
) {
    val podcastRepository = MockPodcastRepository()

    var isLoading by remember { mutableStateOf(true) }
    var podcastPrograms by remember { mutableStateOf<List<PodcastProgram>>(listOf()) }

    LaunchedEffect(null) {
        isLoading = true
        podcastPrograms = podcastRepository.getPodcastPrograms()
        isLoading = false
    }

    if (isLoading) {
        LoadingPage()
        return
    }

    val mediaId = audioController.mediaId.observeAsState()
    val playbackState = audioController.playbackState.observeAsState()
    val onlineRadioPlaybackState =
        if (mediaId.value != LIVE_HITRADIO_ID || playbackState.value == null) {
            AudioStateManager.PlaybackState.STOPPED
        } else {
            playbackState.value!!
        }

    Column() {
        OnlineRadio(
            playbackState = onlineRadioPlaybackState,
            onPlayPause = {
                if (mediaId.value != LIVE_HITRADIO_ID) {
                    audioController.setSource(
                        ChangingMetadataSource(
                            id = LIVE_HITRADIO_ID,
                            name = "Hit rádió",
                            description = "Élő adás",
                            url = SourceUrl(
                                low = "http://stream2.hit.hu:8080/speech",
                                medium = "http://stream2.hit.hu:8080/low",
                                high = "http://stream2.hit.hu:8080/high"
                            ),
                            currentProgramRepository = programRepository
                        )
                    )

                    if (playbackState.value?.isPlaying() != true) {
                        audioController.playPause()
                    }
                } else {
                    audioController.playPause()
                }
            },
            programRepository = programRepository,
        )
        // player bar
        // program list
        PodcastProgramList(navController = navController, programs = podcastPrograms)
    }
}