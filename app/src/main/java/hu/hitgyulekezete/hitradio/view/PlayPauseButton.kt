package hu.hitgyulekezete.hitradio.view

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager

@Composable
fun PlayPauseButton(
    playbackState: AudioStateManager.PlaybackState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(1f)
    ) {
        Icon(
            if (playbackState.isPlaying()) Icons.Default.Pause else Icons.Default.PlayArrow,
            "${if (playbackState.isPlaying()) "Leállítás" else "Folytatás"} gomb",
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f)

        )
    }
}