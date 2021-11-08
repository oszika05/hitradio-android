package hu.hitgyulekezete.hitradio.view.pages.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hu.hitgyulekezete.hitradio.model.news.NewsPagingSource
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.paging.EpisodePagingSource
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class EpisodesPageViewModel: ViewModel() {

    private val repository = ProgramRepository()

    val search = MutableStateFlow<String>("")
    val programId = MutableStateFlow<String?>(null)

    fun reset(
        initialSearch: String,
        programId: String?
    ) {
        if (this.programId.value != programId) {
            this.programId.value = programId
        }
        if (this.search.value != initialSearch) {
            this.search.value = initialSearch
        }
    }

    val episodes: Flow<PagingData<Episode>> = search
        .combine(programId) { search, programId -> search to programId }
        .flatMapLatest { (search, programId) ->
            // debounce
            delay(300L)

            Pager(PagingConfig(5)) {
                EpisodePagingSource(repository, search)
            }
                .flow
                .cachedIn(
                    viewModelScope
                )
        }
        .cachedIn(viewModelScope)
}