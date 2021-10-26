package hu.hitgyulekezete.hitradio.view.pages.news

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items

@Composable
fun NewsPage(
    viewModel: NewsPageViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val search by viewModel.search.collectAsState("")

    val news = viewModel.news.collectAsLazyPagingItems()

    LazyColumn() {
        item(key = "search") {
            TextField(value = search, onValueChange = { viewModel.search.compareAndSet(search, it) })
        }

        if (news.loadState.refresh is LoadState.Loading) {
            item(key = "refresh_loading") {
                CircularProgressIndicator()
            }
        } else {
            items(news, key = { it.id }) { newsItem ->
                Text(newsItem?.title ?: "null", modifier = Modifier.height(165.dp))
            }
        }

        if (news.loadState.append is LoadState.Loading) {
            item(key = "append_loading") {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
fun Preview_NewsPage() {
    NewsPage()
}