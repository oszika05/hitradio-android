package hu.hitgyulekezete.hitradio

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.audio.AudioController
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.view.nowplaying.NowPlayingBar
import hu.hitgyulekezete.hitradio.view.nowplaying.NowPlayingBarExpanded
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val barHeight = 64.dp


@ExperimentalMaterialApi
@Composable
fun NowPlayingBarLayout(
    modifier: Modifier = Modifier,
    metadata: Metadata,
    playbackState: AudioController.PlaybackState,
    seekPercentage: Float,
    volumePercentage: Float,
    onSeekTo: (Float) -> Unit,
    onSetVolume: (Float) -> Unit,
    onPlayPausePressed: () -> Unit,
    content: @Composable () -> Unit
) {
    var layoutHeight by remember { mutableStateOf(1) }

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { barHeight.toPx() }
    val anchors = mapOf(layoutHeight.toFloat() - sizePx to 0, 0f to 1)

    fun getSwipeState(): Float {
        return swipeableState.offset.value / (layoutHeight.toFloat() - sizePx)
    }

    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                layoutHeight = coordinates.size.height
            }
    ) {
        Box(
            Modifier
                .padding(bottom = barHeight)
                .alpha(getSwipeState())
        ) {
            content()
        }

        Box(
            Modifier
                .fillMaxWidth()
                .offset {
                    return@offset IntOffset(0, swipeableState.offset.value.roundToInt())
                }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.2f) },
                    orientation = Orientation.Vertical
                )
        ) {
            Card(
                Modifier
                    .height(barHeight)
                    .fillMaxWidth()
                    .alpha(getSwipeState()),
                elevation = 8.dp,
            ) {
                Column(
                    Modifier.clickable {
                        coroutineScope.launch {
                            swipeableState.animateTo(1)
                        }
                    }
                ) {
                    NowPlayingBar(
                        height = barHeight,
                        playbackState = playbackState,
                        onPlayPausePressed = onPlayPausePressed,
                        metadata = metadata
                    )
                }
            }

            Card(
                Modifier
                    .fillMaxWidth()
                    .alpha(1 - getSwipeState())
            ) {
                NowPlayingBarExpanded(
                    playbackState = playbackState,
                    metadata = metadata,
                    seekPercentage = seekPercentage,
                    volumePercentage = volumePercentage,
                    onPlayPause = onPlayPausePressed,
                    onSeekTo = onSeekTo,
                    onSetVolume = onSetVolume,
                    onClose = {
                        coroutineScope.launch {
                            swipeableState.animateTo(0)
                        }
                    }
                )
            }
        }


    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewNowPlayingBarLayout() {
    var playbackState by remember { mutableStateOf(AudioController.PlaybackState.STOPPED) }
    var volumePercentage by remember { mutableStateOf(0.0f) }
    var seekPercentage by remember { mutableStateOf(0.0f) }

    NowPlayingBarLayout(
        metadata = Metadata(
            title = "Title",
            subtitle = null,
            artUri = null,
            type = MetadataType.LIVE
        ),
        playbackState = playbackState,
        onPlayPausePressed = {
            playbackState = playbackState.toggle()
        },
        volumePercentage = volumePercentage,
        seekPercentage = seekPercentage,
        onSetVolume = {
            volumePercentage = it
        },
        onSeekTo = {
            seekPercentage = it
        }
    ) {
        Text("this is the background")
    }
}