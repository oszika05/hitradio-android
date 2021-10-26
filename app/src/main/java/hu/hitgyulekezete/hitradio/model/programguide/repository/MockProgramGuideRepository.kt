package hu.hitgyulekezete.hitradio.model.programguide.repository

import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem
import java.util.*

class MockProgramGuideRepository(
    private val programs: List<ProgramGuideItem> = listOf(
        ProgramGuideItem(
            id = "1",
            title = "Teszt1",
            start = Calendar.getInstance().run {
                add(Calendar.MINUTE, -30)
                time
            },
            end = Calendar.getInstance().run {
                add(Calendar.MINUTE, 30)
                time
            },
            description = "lorem ipsum",
            replay = ""
        ),
        ProgramGuideItem(
            id = "2",
            title = "Teszt2",
            start = Calendar.getInstance().run {
                add(Calendar.MINUTE, 30)
                time
            },
            end = Calendar.getInstance().run {
                add(Calendar.MINUTE, 90)
                time
            },
            description = "lorem ipsum",
            replay = "(ism.)"
        ),
        ProgramGuideItem(
            id = "3",
            title = "Teszt3",
            start = Calendar.getInstance().run {
                add(Calendar.MINUTE, 90)
                time
            },
            end = Calendar.getInstance().run {
                add(Calendar.MINUTE, 30)
                add(Calendar.HOUR, 4)
                time
            },
            description = "lorem ipsum",
            replay = ""
        ),
    )
) : ProgramGuideRepository {

    override suspend fun getPrograms(): List<ProgramGuideItem> {
        return programs
    }
}