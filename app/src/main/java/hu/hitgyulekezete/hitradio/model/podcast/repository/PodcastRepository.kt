package hu.hitgyulekezete.hitradio.model.podcast.repository

import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram


interface PodcastRepository {
    suspend fun getPodcastPrograms(): List<PodcastProgram>
    suspend fun getPodcastProgram(id: String): PodcastProgram?
    suspend fun getPodcastsInProgram(podcastProgramId: Int, page: Int = 1, pageSize: Int = 50): List<Source>
}