package hu.hitgyulekezete.hitradio.view.pages.live

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.audio.metadata.source.ChangingMetadataSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.live
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem
import hu.hitgyulekezete.hitradio.model.programguide.api.ProgramGuideApi
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.programguide.day
import hu.hitgyulekezete.hitradio.model.programguide.repository.ProgramGuideRepository
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LivePageViewModel @Inject constructor(
    programGuideRepository: ProgramGuideRepository,
) : ViewModel() {

    val programs = flow<List<ProgramGuideItem>> {
        emit(listOf())
        val programs = programGuideRepository.getPrograms()
        Log.d("KORTE", "programs: ${programs.size}")
        emit(programs)
    }.shareIn(
        viewModelScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    val currentProgramRepository = programs.mapLatest { programs ->
        val repo = CurrentProgramRepository(programs)
        repo.start()
        return@mapLatest repo
    }.shareIn(
        viewModelScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    val currentProgram = currentProgramRepository.flatMapLatest { currentProgramRepository ->
        Log.d("KORTE", "currentProgram update ${currentProgramRepository.currentProgram}")
        Log.d(
            "KORTE",
            "currentProgram update ${currentProgramRepository.currentProgramLiveData.value}"
        )
        Log.d("KORTE", "currentProgram update ${currentProgramRepository.getCurrent()}")
//        currentProgramRepository.currentProgramLiveData.asFlow()
        currentProgramRepository.currentOrDefault
    }

    val source = currentProgramRepository.mapLatest { currentProgramRepository ->
        ChangingMetadataSource.live(currentProgramRepository)
    }

    val programsByDay: Flow<Map<String, List<ProgramGuideItem>>> = programs.mapLatest { programs ->
        programs.groupBy { it.day }
    }

    val nextPrograms = currentProgramRepository.flatMapLatest { currentProgramRepository ->
        Log.d("KORTE", "nextPrograms update ${currentProgramRepository.getNext()}")
        currentProgramRepository.nextPrograms.asFlow()
    }
}