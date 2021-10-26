package hu.hitgyulekezete.hitradio.model.programguide

import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class CurrentProgramGuideRepositoryTest {
    @Test
    fun correctlyReturnsCurrentProgram() {

        val currentMockProgram = ProgramGuideItem(
            id = "1",
            title = "1",
            start = Calendar.getInstance().run {
                add(Calendar.MINUTE, -30)
                time
            },
            end = Calendar.getInstance().run {
                add(Calendar.MINUTE, 30)
                time
            },
            description = "",
            replay = "",
        )

        val repo = CurrentProgramRepository(listOf(
            ProgramGuideItem(
                id = "2",
                title = "2",
                start = Calendar.getInstance().run {
                    add(Calendar.MINUTE, -60)
                    time
                },
                end = Calendar.getInstance().run {
                    add(Calendar.MINUTE, -30)
                    time
                },
                description = "",
                replay = "",
            ),
            currentMockProgram,
            ProgramGuideItem(
                id = "3",
                title = "3",
                start = Calendar.getInstance().run {
                    add(Calendar.MINUTE, 30)
                    time
                },
                end = Calendar.getInstance().run {
                    add(Calendar.MINUTE, 60)
                    time
                },
                description = "",
                replay = "",
            ),
        ))

        val currentProgram = repo.getCurrent()

        assertEquals(currentMockProgram, currentProgram)
    }

    @Test
    fun correctlyReturnsNextProgram() {

        val nextMockProgram = ProgramGuideItem(
            id = "1",
            title = "1",
            start = Calendar.getInstance().run {
                add(Calendar.MINUTE, 30)
                time
            },
            end = Calendar.getInstance().run {
                add(Calendar.MINUTE, 60)
                time
            },
            description = "",
            replay = "",
        )

        val repo = CurrentProgramRepository(listOf(
            ProgramGuideItem(
                id = "2",
                title = "2",
                start = Calendar.getInstance().run {
                    add(Calendar.MINUTE, -60)
                    time
                },
                end = Calendar.getInstance().run {
                    add(Calendar.MINUTE, -30)
                    time
                },
                description = "",
                replay = "",
            ),
            nextMockProgram,
            ProgramGuideItem(
                id = "3",
                title = "3",
                start = Calendar.getInstance().run {
                    add(Calendar.MINUTE, -30)
                    time
                },
                end = Calendar.getInstance().run {
                    add(Calendar.MINUTE, 30)
                    time
                },
                description = "",
                replay = "",
            ),
        ))

        val nextProgram = repo.getNext()

        assertEquals(nextMockProgram, nextProgram)
    }
}