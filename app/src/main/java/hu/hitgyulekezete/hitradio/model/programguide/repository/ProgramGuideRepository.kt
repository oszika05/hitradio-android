package hu.hitgyulekezete.hitradio.model.programguide.repository

import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem

interface ProgramGuideRepository {
    suspend fun getPrograms(): List<ProgramGuideItem>
}