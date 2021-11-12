package hu.hitgyulekezete.hitradio.view.components.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.painter.Painter
import hu.hitgyulekezete.hitradio.view.components.button.variants.PrimaryButton
import hu.hitgyulekezete.hitradio.view.components.button.variants.SecondaryButton
import hu.hitgyulekezete.hitradio.view.components.button.variants.TernaryButton

enum class ButtonVariant {
    Primary,
    Secondary,
    Ternary,
}

@Composable
fun Button(
    text: String,
    leftIcon: Painter? = null,
    rightIcon: Painter? = null,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    onClick: () -> Unit = {},
) {
    when (variant) {
        ButtonVariant.Primary -> PrimaryButton(
            text = text,
            leftIcon = leftIcon,
            rightIcon = rightIcon,
            modifier = modifier,
            onClick = onClick,
        )
        ButtonVariant.Secondary -> SecondaryButton(
            text = text,
            leftIcon = leftIcon,
            rightIcon = rightIcon,
            modifier = modifier,
            onClick = onClick,
        )
        ButtonVariant.Ternary -> TernaryButton(
            text = text,
            leftIcon = leftIcon,
            rightIcon = rightIcon,
            modifier = modifier,
            onClick = onClick,
        )
    }
}