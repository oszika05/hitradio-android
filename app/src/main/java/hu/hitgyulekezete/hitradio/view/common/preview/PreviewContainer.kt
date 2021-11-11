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
fun PreviewContainer(body: @Composable () -> Unit) {
    HitradioTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(vertical = 16.dp, horizontal = 16.dp),
        ) {
            body()
        }
    }
}