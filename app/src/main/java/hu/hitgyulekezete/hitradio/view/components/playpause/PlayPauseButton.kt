package hu.hitgyulekezete.hitradio.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.R
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.view.layout.black
import hu.hitgyulekezete.hitradio.view.layout.white

@Composable
fun PlayPauseButton(
    playbackState: AudioStateManager.PlaybackState,
    circleAroundButton: Boolean = false,
    circleBackground: Boolean = false,
    isLight: Boolean = false,
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
        val color = if (isLight) {
            MaterialTheme.colors.white
        } else {
            MaterialTheme.colors.black
        }

        Box(
            contentAlignment = Alignment.Center
        ) {
            if (circleBackground) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(all = if (circleAroundButton) 5.dp else 0.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                if (playbackState == AudioStateManager.PlaybackState.BUFFERING) {
                    CircularProgressIndicator(
                        color = color
                    )
                } else if (playbackState.isPlaying()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = "Leállítás gomb",
                        tint = color,
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Lejátszás gomb",
                        tint = color,
                    )
                }
            }
        }

        if (circleAroundButton) {
            Box(
                Modifier
                    .fillMaxSize()
                    .border(5.dp, color, shape = CircleShape)
            )
        }
    }
}