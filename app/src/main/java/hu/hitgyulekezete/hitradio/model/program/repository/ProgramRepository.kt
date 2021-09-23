package hu.hitgyulekezete.hitradio.model.program.repository

import hu.hitgyulekezete.hitradio.model.program.Program

interface ProgramRepository {
    suspend fun getPrograms(): List<Program>
}