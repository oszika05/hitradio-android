package hu.hitgyulekezete.hitradio.view.common.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.view.layout.HitradioTheme

@Composable
fun PreviewContainer(
    disablePadding: Boolean = false,
    body: @Composable () -> Unit
) {
    HitradioTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(
                    vertical = if (disablePadding) 0.dp else 16.dp,
                    horizontal = if (disablePadding) 0.dp else 16.dp
                ),
        ) {
            body()
        }
    }
}