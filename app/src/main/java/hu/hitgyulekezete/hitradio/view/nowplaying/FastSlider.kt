package hu.hitgyulekezete.hitradio.view.nowplaying

import androidx.compose.material.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun FastSlider(
    value: Float,
    onValueChange: (value: Float) -> Unit,
    continuouslyUpdate: Boolean = false,
    modifier: Modifier = Modifier
) {
    var dragging by remember { mutableStateOf(false) }

    var lastDraggedPosition by remember { mutableStateOf<Float?>(null) }

    var sliderPosition by remember { mutableStateOf(value) }

    LaunchedEffect(dragging, value, lastDraggedPosition) {
        if (dragging) {
            return@LaunchedEffect
        }

        if (lastDraggedPosition == null) {
            sliderPosition = value
        }

        if (lastDraggedPosition != null && lastDraggedPosition!! - value < 0.01f) {
            lastDraggedPosition = null
        }
    }

    Slider(
        value = sliderPosition,
        onValueChange = {
            dragging = true
            sliderPosition = it
            lastDraggedPosition = it
            if (continuouslyUpdate) {
                onValueChange(sliderPosition)
            }
        },
        onValueChangeFinished = {
            if (!continuouslyUpdate) {
                onValueChange(sliderPosition)
            }
            dragging = false
        },
        modifier = modifier
    )
}