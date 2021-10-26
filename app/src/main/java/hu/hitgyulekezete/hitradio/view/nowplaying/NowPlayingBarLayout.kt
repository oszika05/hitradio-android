package hu.hitgyulekezete.hitradio.view.nowplaying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val barHeight = 64.dp


@ExperimentalMaterialApi
@Composable
fun NowPlayingBarLayout(
    modifier: Modifier = Modifier,
    metadata: Metadata,
    playbackState: AudioStateManager.PlaybackState,
    seekPercentage: Float?,
    volumePercentage: Float,
    onSeekTo: (Float) -> Unit,
    onSetVolume: (Float) -> Unit,
    onPlayPausePressed: () -> Unit,
    swipeableState: SwipeableState<Int> = rememberSwipeableState(0),
    content: @Composable () -> Unit
) {
    var layoutHeight by remember { mutableStateOf(1) }

    val sizePx = with(LocalDensity.current) { barHeight.toPx() }
    val anchors = mapOf(layoutHeight.toFloat() - sizePx to 0, 0f to 1)

    fun getSwipeState(): Float {
        return swipeableState.offset.value / (layoutHeight.toFloat() - sizePx)
    }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier
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
                    NowPlayingBarCollapsed(
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
    var playbackState by remember { mutableStateOf(AudioStateManager.PlaybackState.STOPPED) }
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