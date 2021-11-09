package hu.hitgyulekezete.hitradio.view.pages.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.program.PersonType
import hu.hitgyulekezete.hitradio.model.program.paging.PersonPagingSource
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PeoplePageViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
): ViewModel() {

    val search = MutableStateFlow<String>("")

    val guests = search.flatMapLatest { search ->
        if (search.isNotEmpty()) {
            delay(300L)
        }

        val q = if (search.isBlank()) null else search

        Pager(PagingConfig(20)) {
            PersonPagingSource(programRepository, q, type = PersonType.Guest)
        }.flow
    }.cachedIn(viewModelScope)

    val hosts = search.flatMapLatest { search ->
        if (search.isNotEmpty()) {
            delay(300L)
        }

        val q = if (search.isBlank()) null else search

        Pager(PagingConfig(20)) {
            PersonPagingSource(programRepository, q, type = PersonType.Host)
        }.flow
    }.cachedIn(viewModelScope)
}