package hu.hitgyulekezete.hitradio.model.podcast.repository

import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram

class MockPodcastRepository : PodcastRepository {
    override suspend fun getPodcastPrograms(): List<PodcastProgram> {
        return listOf(
            PodcastProgram.test1,
            PodcastProgram.test2,
        )
    }

    override suspend fun getPodcastProgram(id: String): PodcastProgram? {
        return listOf(
            PodcastProgram.test1,
            PodcastProgram.test2,
        ).find { it.id == id }
    }

    override suspend fun getPodcastsInProgram(
        podcastProgramId: Int,
        page: Int,
        pageSize: Int
    ): List<Source> {
        val podcasts = PodcastProgram.test1.podcasts + PodcastProgram.test2.podcasts

        if (page >= podcasts.size) {
            return listOf()
        }

        val toIndex = (page + pageSize).coerceAtMost(podcasts.size)

        return podcasts.subList(page, toIndex)
    }
}