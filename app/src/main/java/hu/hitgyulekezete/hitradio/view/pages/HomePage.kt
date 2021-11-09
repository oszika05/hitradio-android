package hu.hitgyulekezete.hitradio.view.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.metadata.source.ChangingMetadataSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.LIVE_ID
import hu.hitgyulekezete.hitradio.audio.metadata.source.live
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram
import hu.hitgyulekezete.hitradio.model.podcast.repository.MockPodcastRepository
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.programguide.api.ProgramGuideApi
import hu.hitgyulekezete.hitradio.view.OnlineRadio
import hu.hitgyulekezete.hitradio.view.podcast.PodcastProgramList

@Composable
fun HomePage(
    navController: NavHostController,
    programApi: ProgramGuideApi,
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

    val mediaId = audioController.mediaId.collectAsState()
    val playbackState = audioController.playbackState.collectAsState()
    val onlineRadioPlaybackState =
        if (mediaId.value != ChangingMetadataSource.LIVE_ID || playbackState.value == null) {
            AudioStateManager.PlaybackState.STOPPED
        } else {
            playbackState.value!!
        }

    Column() {
        OnlineRadio(
            playbackState = onlineRadioPlaybackState,
            onPlayPause = {
                if (mediaId.value != ChangingMetadataSource.LIVE_ID) {
                    audioController.setSource(
                        ChangingMetadataSource.live(programRepository)
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