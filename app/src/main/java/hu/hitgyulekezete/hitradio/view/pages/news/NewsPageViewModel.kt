package hu.hitgyulekezete.hitradio.view.pages.news

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.model.news.NewsPagingSource
import hu.hitgyulekezete.hitradio.model.news.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class NewsPageViewModel @Inject constructor() : ViewModel() {

    private val repository = NewsRepository()

    val search = MutableStateFlow<String>("")

    val news: Flow<PagingData<News>> = search
        .flatMapLatest { search ->
            // debounce
            delay(300L)

            Pager(PagingConfig(5)) {
                NewsPagingSource(repository, search)
            }
                .flow
                .cachedIn(
                    viewModelScope
                )
        }
        .cachedIn(viewModelScope)

}