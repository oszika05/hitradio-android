package hu.hitgyulekezete.hitradio.view.pages.episode

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EpisodePageViewModel @Inject constructor(
    private val programRepository: ProgramRepository
) : ViewModel() {
    val episodeId = MutableStateFlow<String?>(null)

    val episode: Flow<Episode?> = episodeId
        .mapLatest { episodeId ->
            if (episodeId == null) {
                return@mapLatest null
            }

            return@mapLatest programRepository.getEpisode(episodeId)
        }

    val related: Flow<List<Episode>> = episode
        .mapLatest { episode ->
            if (episode == null) {
                return@mapLatest listOf<Episode>()
            }

            return@mapLatest programRepository.getRelatedEpisodes(episode!!.id)
        }
}