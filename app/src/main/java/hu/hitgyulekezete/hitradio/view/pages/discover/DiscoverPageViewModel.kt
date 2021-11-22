package hu.hitgyulekezete.hitradio.view.pages.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.program.paging.EpisodePagingSource
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class DiscoverPageViewModel @Inject constructor(
    programRepository: ProgramRepository,
): ViewModel() {

    val search = MutableStateFlow("")

    val programs = search.mapLatest { search ->
        if (search.isNotBlank()) {
            delay(300L)
        }

        programRepository.getPrograms(0, 15, search)
    }

    val people = search.mapLatest { search ->
        if (search.isNotBlank()) {
            delay(300L)
        }

        programRepository.getPeople(0, 15, search, type = null)
    }

    val episodes = search.flatMapLatest { search ->
        if (search.isNotBlank()) {
            delay(300L)
        }

        Pager(PagingConfig(20)) {
            EpisodePagingSource(programRepository, search = search)
        }.flow
    }.cachedIn(viewModelScope)


}