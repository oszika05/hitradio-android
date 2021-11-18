package hu.hitgyulekezete.hitradio.view.nowplaying

import android.media.AudioManager
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hu.hitgyulekezete.hitradio.audio.VolumeObserver
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.view.Pages
import kotlin.math.roundToInt
import androidx.compose.ui.Modifier
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager

@ExperimentalMaterialApi
@Composable
fun NowPlayingBar(
    audioController: AudioController,
    audioManager: AudioManager,
    volumeObserver: VolumeObserver,
    modifier: Modifier = Modifier,
    swipeableState: SwipeableState<Int>  = rememberSwipeableState(0),
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val metadata by audioController.metadata.collectAsState()
    val playbackState by audioController.playbackState.collectAsState()
    val seekPosition by audioController.seekPosition.collectAsState()
    val volume by volumeObserver.volume.observeAsState(0.0f)

    Log.d("ALMA", "outside layout $metadata")

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
        },
        modifier = modifier,
        swipeableState = swipeableState,
    ) {
        content()
    }
}