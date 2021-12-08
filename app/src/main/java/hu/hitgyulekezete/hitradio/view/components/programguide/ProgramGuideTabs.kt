package hu.hitgyulekezete.hitradio.view.components.programguide

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.TabRow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import kotlinx.coroutines.delay
import java.util.*

private val days = listOf("H", "K", "Sz", "Cs", "P", "Sz", "V")


@Composable
fun ProgramGuideTabs(
    programs: List<ProgramGuideItem>
) {
    var currentTab by remember { mutableStateOf(0) }

    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(currentTime) {
        delay(1000L)

        currentTime = Date()
    }

    Column() {
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            days.forEachIndexed { index, title ->
                ProgramGuideDayTab(
                    day = title,
                    isSelected = index == currentTab,
                    onClick = { currentTab = index }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Crossfade(targetState = currentTab) { screen ->
            val programsForDay = programs.filter { program ->
                val dayOfWeek =
                    Calendar.getInstance().apply { time = program.start }.get(Calendar.DAY_OF_WEEK)

                screen == when (dayOfWeek) {
                    Calendar.MONDAY -> 0
                    Calendar.TUESDAY -> 1
                    Calendar.WEDNESDAY -> 2
                    Calendar.THURSDAY -> 3
                    Calendar.FRIDAY -> 4
                    Calendar.SATURDAY -> 5
                    Calendar.SUNDAY -> 6
                    else -> 7
                }
            }

            Column(Modifier.horizontalScroll(rememberScrollState())) {
                programsForDay.forEach { program ->
                    hu.hitgyulekezete.hitradio.view.components.programguide.ProgramGuideItem(
                        title = program.title,
                        description = program.description,
                        time = program.start,
                        isLive = !program.start.after(currentTime) && program.end.after(currentTime)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview_ProgramGuideTabs() {
    PreviewContainer {
        ProgramGuideTabs(
            listOf(
                ProgramGuideItem(
                    id = "1",
                    title = "1",
                    start = Calendar.getInstance().run {
                        time
                    },
                    end = Calendar.getInstance().run {
                        add(Calendar.MINUTE, 1)
                        time
                    },
                    description = "fdskjfsldkfjldsakjfasdlf",
                    replay = "",
                ),
                ProgramGuideItem(
                    id = "2",
                    title = "2",
                    start = Calendar.getInstance().run {
                        add(Calendar.MINUTE, 1)
                        time
                    },
                    end = Calendar.getInstance().run {
                        add(Calendar.MINUTE, 2)
                        time
                    },
                    description = "fdskjfsldkfjldsakjfasdlf",
                    replay = "",
                ),
            )
        )
    }
}