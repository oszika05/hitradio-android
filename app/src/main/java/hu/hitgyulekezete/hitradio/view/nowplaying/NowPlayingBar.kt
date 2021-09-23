package hu.hitgyulekezete.hitradio.view.nowplaying

import android.media.AudioManager
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hu.hitgyulekezete.hitradio.audio.VolumeObserver
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.view.Pages
import kotlin.math.roundToInt
import androidx.compose.runtime.getValue
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager

@ExperimentalMaterialApi
@Composable
fun NowPlayingBar(
    navController: NavController,
    audioController: AudioController,
    audioManager: AudioManager,
    volumeObserver: VolumeObserver,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val metadata by audioController.metadata.observeAsState()
    val playbackState by audioController.playbackState.observeAsState()
    val seekPosition by audioController.seekPosition.observeAsState()
    val volume by volumeObserver.volume.observeAsState(0.0f)

    NowPlayingBarLayout(
        metadata = metadata ?: Metadata.Empty,
        playbackState = playbackState ?: AudioStateManager.PlaybackState.STOPPED,
        seekPercentage = seekPosition,
        volumePercentage = volume,
        onSeekTo = {
            audioController.seekTo(it)
        },
        onSetVolume = {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                (it * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).roundToInt(),
                0
            )
        },
        onPlayPausePressed = {
            audioController.playPause()
        }) {

        content()
    }
}