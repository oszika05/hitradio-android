package hu.hitgyulekezete.hitradio.view.layout

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val Colors.success: Color
    @Composable get() = if (isLight) Color(0xffaee571) else Color(0xffaee571)

val Colors.info: Color
    @Composable get() = if (isLight) Color(0xffb3e5fc) else Color(0xffb3e5fc)

val Colors.warning: Color
    @Composable get() = if (isLight) Color(0xffffad42) else Color(0xffffad42)

val Colors.shadow: Color
    @Composable get() = if (isLight) Color(0x20000000) else Color(0x00000000)

val Colors.primaryText: Color
    @Composable get() = if (isLight) Color(0xff000000) else Color(0xff000000)

val Colors.secondaryText: Color
    @Composable get() = if (isLight) Color(0xff737373) else Color(0xff737373)

private val LightColors = lightColors(
    primary = Color(0xff007AFF),
    primaryVariant = Color(0xff007AFF),
    secondary = Color(0xff007AFF),
    secondaryVariant = Color(0xff007AFF),
    background = Color(0xffE5E5E5),
    surface = Color(0xffffffff),
    error = Color(0xffff6f60),
    onPrimary = Color(0xffffffff),
    onSecondary = Color(0xffffffff),
    onBackground = Color(0xff000000),
    onSurface = Color(0xff202039),
    onError = Color(0xff000000),
)

private val DarkColors = darkColors(
    primary = Color(0xff007AFF),
    primaryVariant = Color(0xff007AFF),
    secondary = Color(0xff007AFF),
    secondaryVariant = Color(0xff007AFF),
    background = Color(0xffE5E5E5),
    surface = Color(0xffffffff),
    error = Color(0xffff6f60),
    onPrimary = Color(0xffffffff),
    onSecondary = Color(0xffc9ced3),
    onBackground = Color(0xffffffff),
    onSurface = Color(0xffffffff),
    onError = Color(0xffffffff),
)

private val typography = Typography(
    h1 = TextStyle(
        fontSize = 34.sp,
        fontWeight = FontWeight.Bold,
    ),
    h2 = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
    ),
    h3 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
    ),
    subtitle1 = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Normal,
    ),
    subtitle2 = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
    ),
    caption = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
    ),
    body1 = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
    ),
    body2 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
    ),
    button = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
    ),
)

@Composable
fun HitradioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = typography,
        content = content,
    )
}