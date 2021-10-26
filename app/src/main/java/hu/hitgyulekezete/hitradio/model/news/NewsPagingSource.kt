package hu.hitgyulekezete.hitradio.model.news

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.model.podcast.repository.PodcastRepository
import java.lang.Exception

class NewsPagingSource(
    private val repository: NewsRepository,
    private val search: String? = null,
    private val tags: List<String> = listOf()
) : PagingSource<Int, News>() {

        override fun getRefreshKey(state: PagingState<Int, News>): Int? {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
            val from = params.key ?: 0

            return try {
                val news = repository.getNews(from, params.loadSize, search, tags)
                val areThereMoreItems = news.size == params.loadSize

                Log.d("ALMA", "from: $from, loadSize: ${params.loadSize}, newsLen: ${news.size}, areThereMoreItems: $areThereMoreItems")

                return LoadResult.Page(
                    news,
                    if (from > 0) (from - params.loadSize).coerceAtLeast(0) else null,
                    if (areThereMoreItems) from + params.loadSize else null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                LoadResult.Error(e)
            }
        }
}