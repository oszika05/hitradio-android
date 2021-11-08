package hu.hitgyulekezete.hitradio.view.pages

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.controller.DownloadManager
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram
import hu.hitgyulekezete.hitradio.model.podcast.repository.MockPodcastRepository
import hu.hitgyulekezete.hitradio.model.podcast.repository.PodcastRepository
import hu.hitgyulekezete.hitradio.view.podcast.program.PodcastProgram

@Composable
fun PodcastProgramPage(
    podcastProgramId: String,
    audioController: AudioController,
    downloadManager: DownloadManager,
) {
    val podcastRepository = MockPodcastRepository()

    val currentMediaId = audioController.mediaId.collectAsState()
    val playbackState = audioController.playbackState.collectAsState()

    var isLoading by remember { mutableStateOf(true) }
    var podcastProgram by remember { mutableStateOf<PodcastProgram?>(null) }

    LaunchedEffect(podcastProgramId) {
        isLoading = true
        podcastProgram = podcastRepository.getPodcastProgram(podcastProgramId)
        isLoading = false
    }

    if (!isLoading && podcastProgram == null) {
        NotFoundPage()
        return
    }

    if (podcastProgram == null || isLoading) {
        LoadingPage()
        return
    }

    PodcastProgram(
        podcastProgram = podcastProgram!!,
        onPlay = { source ->
            if (source.id == currentMediaId.value) {
                audioController.playPause()
            } else {
                audioController.setSource(source)
                if (audioController.playbackState.value?.isPlaying() != true) {
                    audioController.playPause()
                }
            }
        },
        currentMediaId = currentMediaId.value,
        playbackState = playbackState.value ?: AudioStateManager.PlaybackState.STOPPED,
        getIsDownloaded = { source ->
            downloadManager.isDownloaded(source)
        },
        downloadPodcast = { source ->
            downloadManager.downloadContent(source)
        }
    )
}