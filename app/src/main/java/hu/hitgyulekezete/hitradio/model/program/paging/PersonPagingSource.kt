package hu.hitgyulekezete.hitradio.model.program.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.PersonType
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import java.lang.Exception

class PersonPagingSource(
    private val repository: ProgramRepository,
    private val search: String? = null,
    private val type: PersonType? = null
): PagingSource<Int, Person>() {
    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val from = params.key ?: 0

        return try {
            val people = repository.getPeople(from, params.loadSize, search, type)
            val areThereMoreItems = people.size == params.loadSize

            return LoadResult.Page(
                people,
                if (from > 0) (from - params.loadSize).coerceAtLeast(0) else null,
                if (areThereMoreItems) from + params.loadSize else null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}