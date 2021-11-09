package hu.hitgyulekezete.hitradio.view.pages.person

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.paging.EpisodePagingSource
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PersonPageViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
): ViewModel() {

    val personId = MutableStateFlow<String?>(null)

    val person: Flow<Person?> = personId.mapLatest { personId ->
        personId ?: return@mapLatest null

        programRepository.getPerson(personId)
    }

    val episodes = personId.flatMapLatest { personId ->
        personId ?: return@flatMapLatest flow<PagingData<Episode>> {
            emit(PagingData.empty())
        }

        return@flatMapLatest Pager(PagingConfig(20)) {
            EpisodePagingSource(programRepository, people = listOf(personId))
        }.flow
    }
}