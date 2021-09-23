package hu.hitgyulekezete.hitradio.model.program.repository

import hu.hitgyulekezete.hitradio.model.program.Program
import java.util.*

class MockProgramRepository(
    private val programs: List<Program> = listOf(
        Program(
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
        Program(
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
        Program(
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
) : ProgramRepository {

    override suspend fun getPrograms(): List<Program> {
        return programs
    }
}