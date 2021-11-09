package hu.hitgyulekezete.hitradio.view.pages.program

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.paging.EpisodePagingSource
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class ProgramPageViewModel @Inject constructor(
    programRepository: ProgramRepository,
): ViewModel() {
    val programId = MutableStateFlow<String?>(null)

    val program = programId.mapLatest { programId ->
        programId ?: return@mapLatest null

        return@mapLatest programRepository.getProgram(programId)
    }

    val episodes = programId.flatMapLatest { programId ->
        if (programId == null) {
            return@flatMapLatest flow<PagingData<Episode>> {
                emit(PagingData.empty())
            }
        }

        return@flatMapLatest Pager(PagingConfig(15)) {
            EpisodePagingSource(
                repository = programRepository,
                programId = programId
            )
        }.flow
    }.cachedIn(viewModelScope)
}