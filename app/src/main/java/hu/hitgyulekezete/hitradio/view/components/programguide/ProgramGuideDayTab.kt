package hu.hitgyulekezete.hitradio.view.components.programguide

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.layout.navBarColor
import hu.hitgyulekezete.hitradio.view.layout.primaryText
import hu.hitgyulekezete.hitradio.view.layout.secondaryText

@Composable
fun ProgramGuideDayTab(
    day: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        Modifier
            .size(38.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.navBarColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            day,
            style = MaterialTheme.typography.body1,
            color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryText,
        )
    }
}

@Preview
@Composable
fun Preview_ProgramGuideDayTab() {
    PreviewContainer() {
        Row(
            Modifier
            .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            listOf("H", "K", "Sz", "Cs", "P", "Sz", "V").map {
                ProgramGuideDayTab(it, isSelected = it == "H")
            }
        }
    }
}