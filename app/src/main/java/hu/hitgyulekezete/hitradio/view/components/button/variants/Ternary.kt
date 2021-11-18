package hu.hitgyulekezete.hitradio.view.components.button.variants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.R
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer

@Composable
fun TernaryButton(
    text: String,
    leftIcon: Painter? = null,
    rightIcon: Painter? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Inner(text, leftIcon, rightIcon)
    }
}

@Preview
@Composable
fun Preview_TernaryButton() {
    PreviewContainer {
        Column {
            TernaryButton(
                text = "Test"
            )

            Spacer(Modifier.height(8.dp))

            TernaryButton(
                text = "Test",
                leftIcon = painterResource(id = R.drawable.ic_download),
            )

            Spacer(Modifier.height(8.dp))


            TernaryButton(
                text = "Test",
                rightIcon = painterResource(id = R.drawable.ic_download),
            )
        }
    }
}