package hu.hitgyulekezete.hitradio.view.pages

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import hu.hitgyulekezete.hitradio.audio.AudioController
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram
import hu.hitgyulekezete.hitradio.model.podcast.PodcastRepository
import hu.hitgyulekezete.hitradio.view.podcast.program.PodcastProgram
import kotlinx.coroutines.launch

@Composable
fun PodcastProgramPage(
    podcastProgramId: String,
    audioController: AudioController,
) {
    val podcastRepository = PodcastRepository()

    val currentMediaId = audioController.mediaId.observeAsState()
    val playbackState = audioController.playbackState.observeAsState()

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
        playbackState = playbackState.value ?: AudioController.PlaybackState.STOPPED,
        getIsDownloaded = { source ->
            audioController.isDownloaded(source)
        },
        downloadPodcast = { source ->
            audioController.downloadContent(source)
        }
    )
}