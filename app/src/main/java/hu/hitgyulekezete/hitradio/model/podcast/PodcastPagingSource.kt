package hu.hitgyulekezete.hitradio.model.podcast

import androidx.paging.PagingSource
import androidx.paging.PagingState
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.model.podcast.repository.PodcastRepository
import java.lang.Exception

class PodcastPagingSource(private val podcastProgramId: Int, private val repository: PodcastRepository) : PagingSource<Int, Source>() {
    override fun getRefreshKey(state: PagingState<Int, Source>): Int? {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Source> {
        val page = params.key ?: 1

        return try {
            val podcasts = repository.getPodcastsInProgram(podcastProgramId, page, params.loadSize)
            val areThereMoreItems = podcasts.size < params.loadSize

            return LoadResult.Page(podcasts, page, areThereMoreItems?.let { page + 1 })
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}