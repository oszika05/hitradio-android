package hu.hitgyulekezete.hitradio.model.podcast


class PodcastRepository {

    // TODO network

    val podcastPrograms: List<PodcastProgram> = listOf(
        PodcastProgram.test1,
        PodcastProgram.test2,
    )

    suspend fun getPodcastPrograms(): List<PodcastProgram> {
        return podcastPrograms
    }

    suspend fun getPodcastProgram(id: String): PodcastProgram? {
        return podcastPrograms.find { p -> p.id == id }
    }
}