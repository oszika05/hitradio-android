package hu.hitgyulekezete.hitradio.view.pages.newsitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.model.news.NewsPagingSource
import hu.hitgyulekezete.hitradio.model.news.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class NewsItemPageViewModel : ViewModel() {

}