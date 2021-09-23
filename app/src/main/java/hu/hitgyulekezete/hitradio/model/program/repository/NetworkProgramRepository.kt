package hu.hitgyulekezete.hitradio.model.program.repository

import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.api.ProgramApi

class NetworkProgramRepository(
    private val programApi: ProgramApi,
) : ProgramRepository {
    override suspend fun getPrograms(): List<Program> {
        return programApi.get()
    }
}