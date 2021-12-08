package hu.hitgyulekezete.hitradio.view.pages.live

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.*
import hu.hitgyulekezete.hitradio.audio.controller.sourcePlaybackState
import hu.hitgyulekezete.hitradio.audio.metadata.source.ChangingMetadataSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.LIVE_ID
import hu.hitgyulekezete.hitradio.audio.metadata.source.live
import hu.hitgyulekezete.hitradio.model.programguide.startHourString
import hu.hitgyulekezete.hitradio.view.PlayPauseButton
import hu.hitgyulekezete.hitradio.view.components.programguide.ProgramGuideTabs
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import hu.hitgyulekezete.hitradio.view.nowplaying.NowPlayingPadding

@Composable
fun LivePage(
    audioController: AudioController,
    viewModel: LivePageViewModel = hiltViewModel(),
) {
    val currentProgram by viewModel.currentProgram.collectAsState(null)
    val source by viewModel.source.collectAsState(null)
    val programs by viewModel.programs.collectAsState(initial = listOf())
    val programsByDay by viewModel.programsByDay.collectAsState(mapOf())
    val nextPrograms by viewModel.nextPrograms.collectAsState(listOf())

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Élő adás",
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.primaryText,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )

        currentProgram?.let { currentProgram ->
            Image(
                rememberImagePainter("https://myonlineradio.hu/public/uploads/radio_img/hit-radio/play_250_250.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Text(
                currentProgram.titleWithReplay,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.primaryText,
                modifier = Modifier.padding(top = 16.dp)
            )

            val playbackStateFlow =
                audioController.sourcePlaybackStateById(ChangingMetadataSource.LIVE_ID)
            val playbackState by playbackStateFlow.collectAsState(initial = AudioStateManager.PlaybackState.STOPPED)

            source?.let { source ->
                Box(Modifier.height(42.dp), contentAlignment = Alignment.Center) {
                    PlayPauseButton(playbackState = playbackState) {
                        audioController.playPauseForSource(source)
                    }
                }
            }
        }

        Text(
            "Műsorújság",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primaryText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp)
        )

        ProgramGuideTabs(programs = programs)

        NowPlayingPadding()
    }

}