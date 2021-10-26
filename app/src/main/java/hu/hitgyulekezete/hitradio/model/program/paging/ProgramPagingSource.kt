package hu.hitgyulekezete.hitradio.model.program.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import java.lang.Exception

class ProgramPagingSource(
    private val repository: ProgramRepository,
    private val search: String?,
): PagingSource<Int, Program>() {
    override fun getRefreshKey(state: PagingState<Int, Program>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Program> {
        val from = params.key ?: 0

        return try {
            val programs = repository.getPrograms(from, params.loadSize, search)
            val areThereMoreItems = programs.size == params.loadSize

            return LoadResult.Page(
                programs,
                if (from > 0) (from - params.loadSize).coerceAtLeast(0) else null,
                if (areThereMoreItems) from + params.loadSize else null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}