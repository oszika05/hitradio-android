package hu.hitgyulekezete.hitradio.view.pages.newsitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.model.news.NewsPagingSource
import hu.hitgyulekezete.hitradio.model.news.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel()
class NewsItemPageViewModel @Inject constructor(
    newsRepository: NewsRepository,
) : ViewModel() {
    val newsId = MutableStateFlow<String?>(null)

    val news = newsId.mapLatest { newsId ->
        if (newsId == null) {
            return@mapLatest null
        }

        newsRepository.getNewsItem(newsId)
    }
}