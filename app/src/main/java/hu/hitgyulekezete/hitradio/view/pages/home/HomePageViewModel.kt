package hu.hitgyulekezete.hitradio.view.pages.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.news.NewsRepository
import hu.hitgyulekezete.hitradio.model.program.repository.ProgramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    programRepository: ProgramRepository,
    newsRepository: NewsRepository,
) : ViewModel() {

    private val refreshDate = MutableStateFlow<Date>(Date())

    private val _isLoading = MutableStateFlow(setOf<Loading>())

    val isLoading: StateFlow<Set<Loading>> = _isLoading

    val episodes = refreshDate.map {
        _isLoading.compareAndSet(_isLoading.value, _isLoading.value + Loading.Episodes)

        val episodes = programRepository.getEpisodes(0, 3)

        _isLoading.compareAndSet(_isLoading.value, _isLoading.value - Loading.Episodes)

        episodes
    }

    val news = refreshDate.map {
        _isLoading.compareAndSet(_isLoading.value, _isLoading.value + Loading.News)

        val news = newsRepository.getNews(0, 3)

        _isLoading.compareAndSet(_isLoading.value, _isLoading.value - Loading.News)

        news
    }

    val people = refreshDate.map {
        _isLoading.compareAndSet(_isLoading.value, _isLoading.value + Loading.People)

        val people = programRepository.getPeople(0, 15)

        _isLoading.compareAndSet(_isLoading.value, _isLoading.value - Loading.People)

        people
    }

    enum class Loading {
        Episodes,
        News,
        People,
    }
}