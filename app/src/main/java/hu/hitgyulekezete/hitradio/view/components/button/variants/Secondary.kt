package hu.hitgyulekezete.hitradio.view.components.button.variants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.R
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.button.Button
import hu.hitgyulekezete.hitradio.view.components.button.ButtonVariant

@Composable
fun SecondaryButton(
    text: String,
    leftIcon: Painter? = null,
    rightIcon: Painter? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    OutlinedButton(onClick = onClick, modifier = modifier) {
        Inner(text, leftIcon, rightIcon)
    }
}

@Preview
@Composable
fun Preview_SecondaryButton() {
    PreviewContainer {
        Column {
            SecondaryButton(
                text = "Test"
            )

            Spacer(Modifier.height(8.dp))

            SecondaryButton(
                text = "Test",
                leftIcon = painterResource(id = R.drawable.ic_download),
            )

            Spacer(Modifier.height(8.dp))


            SecondaryButton(
                text = "Test",
                rightIcon = painterResource(id = R.drawable.ic_download),
            )
        }
    }
}
