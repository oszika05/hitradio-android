package hu.hitgyulekezete.hitradio.view.pages.programs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.paging.ProgramPagingSource
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ProgramsPageViewModel @Inject constructor(
    programRepository: ProgramRepository
) : ViewModel() {

    val search = MutableStateFlow<String?>(null)

    val programs = search.flatMapLatest { search ->
        return@flatMapLatest Pager(PagingConfig(20)) {
            ProgramPagingSource(programRepository, search)
        }.flow
    }.cachedIn(viewModelScope)

}