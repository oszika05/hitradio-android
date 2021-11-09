package hu.hitgyulekezete.hitradio.view.pages.live

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.controller.playPauseForSource
import hu.hitgyulekezete.hitradio.audio.controller.sourcePlaybackState
import hu.hitgyulekezete.hitradio.audio.metadata.source.ChangingMetadataSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.LIVE_ID
import hu.hitgyulekezete.hitradio.model.programguide.startHourString
import hu.hitgyulekezete.hitradio.view.PlayPauseButton

@Composable
fun LivePage(
    audioController: AudioController,
    viewModel: LivePageViewModel = hiltViewModel(),
) {
    val currentProgram by viewModel.currentProgram.collectAsState(null)
    val source by viewModel.source.collectAsState(null)
    val programsByDay by viewModel.programsByDay.collectAsState(mapOf())
    val nextPrograms by viewModel.nextPrograms.collectAsState(listOf())

    Column() {
        currentProgram?.let { currentProgram ->
            Text(currentProgram.titleWithReplay)

            val playbackStateFlow =
                audioController.sourcePlaybackState(ChangingMetadataSource.LIVE_ID)
            val playbackState by playbackStateFlow.collectAsState(initial = AudioStateManager.PlaybackState.STOPPED)

            source?.let { source ->
                Box(Modifier.height(42.dp), contentAlignment = Alignment.Center) {
                    PlayPauseButton(playbackState = playbackState) {
                        audioController.playPauseForSource(source)
                    }
                }
            }
        }

        nextPrograms
            .filterIndexed { index, _ -> index < 3 }
            .forEach { program ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(program.startHourString)
                    Text(program.title)
                }

            }
    }

}