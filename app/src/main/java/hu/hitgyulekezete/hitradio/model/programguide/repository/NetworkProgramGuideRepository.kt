package hu.hitgyulekezete.hitradio.model.programguide.repository

import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem
import hu.hitgyulekezete.hitradio.model.programguide.api.ProgramGuideApi

class NetworkProgramGuideRepository(
    private val programApi: ProgramGuideApi,
) : ProgramGuideRepository {
    override suspend fun getPrograms(): List<ProgramGuideItem> {
        return programApi.get()
    }
}