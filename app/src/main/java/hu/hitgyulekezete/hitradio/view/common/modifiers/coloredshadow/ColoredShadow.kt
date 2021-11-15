package hu.hitgyulekezete.hitradio.view.common.modifiers.coloredshadow

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.view.layout.shadow

private fun red(color: Long): Float {
    return if (color and 0x3fL == 0L) {
        (color shr 48 and 0xff) / 255.0f
    } else {
        (color shr 48 and 0xffff).toFloat()
    }
}

private fun green(color: Long): Float {
    return if (color and 0x3fL == 0L) {
        (color shr 40 and 0xff) / 255.0f
    } else {
        (color shr 32 and 0xffff).toFloat()
    }
}

private fun blue(color: Long): Float {
    return if (color and 0x3fL == 0L) {
        (color shr 32 and 0xff) / 255.0f
    } else {
        (color shr 16 and 0xffff).toFloat()
    }
}

private fun alpha(color: Long): Float {
    return if (color and 0x3fL == 0L) {
        (color shr 56 and 0xff) / 255.0f
    } else {
        (color shr 6 and 0x3ff) / 1023.0f
    }
}

private fun toArgbCompat(color: Long): Int {
    if (color and 0x3fL == 0L) return (color shr 32).toInt()

    val r = red(color).toFloat()
    val g = green(color).toFloat()
    val b = blue(color).toFloat()
    val a = alpha(color).toFloat()

    return (a * 255.0f + 0.5f).toInt() shl 24 or
            ((r * 255.0f + 0.5f).toInt() shl 16) or
            ((g * 255.0f + 0.5f).toInt() shl 8) or
            (b * 255.0f + 0.5f).toInt()
}

@Composable
fun Modifier.coloredShadow(
    color: Color = MaterialTheme.colors.shadow,
    alpha: Float = 0.2f,
    borderRadius: Dp = 43.dp,
//    borderRadius: Dp = 0.dp,
//    shadowRadius: Dp = 16.dp,
    shadowRadius: Dp = -(8.dp),
    offsetY: Dp = 17.dp,
    offsetX: Dp = 0.dp,
): Modifier {
    if (!MaterialTheme.colors.isLight) {
        return this
    }

    return this.drawBehind {
        val transparentColor = toArgbCompat(color.copy(alpha = 0f).value.toLong())
        val shadowColor = toArgbCompat(color.copy(alpha = alpha).value.toLong())

        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparentColor
            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )

            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}