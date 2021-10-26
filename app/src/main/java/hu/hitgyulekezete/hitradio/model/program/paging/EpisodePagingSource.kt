package hu.hitgyulekezete.hitradio.model.program.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import java.lang.Exception

class EpisodePagingSource(
    private val repository: ProgramRepository,
    private val programId: String? = null,
    private val search: String? = null,
    private val tags: List<String> = listOf(),
    private val people: List<String> = listOf()
): PagingSource<Int, Episode>() {
    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        val from = params.key ?: 0

        return try {
            val episodes = repository.getEpisodes(from, params.loadSize, programId, search, tags, people)
            val areThereMoreItems = episodes.size == params.loadSize

            return LoadResult.Page(
                episodes,
                if (from > 0) (from - params.loadSize).coerceAtLeast(0) else null,
                if (areThereMoreItems) from + params.loadSize else null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}