package hu.hitgyulekezete.hitradio.view.pages.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.view.components.news.newscard.NewsCard
import hu.hitgyulekezete.hitradio.view.components.textfield.TextField
import hu.hitgyulekezete.hitradio.view.nowplaying.nowPlayingPadding
import java.util.*
import java.util.concurrent.TimeUnit

@ExperimentalFoundationApi
fun <T : Any, G> LazyListScope.groupedByItems(
    items: LazyPagingItems<T>,
    groupBy: (item: T) -> G,
    key: (item: T) -> Any,
    header: @Composable (group: G) -> Unit,
    content: @Composable (item: T) -> Unit,
) {
    val groups: MutableMap<G, List<T>> = mutableMapOf()
    for (i in 0 until items.itemCount) {
        val item = items[i] ?: continue

        val group = groupBy(item)

        if (!groups.containsKey(group)) {
            groups[group] = listOf()
        }

        groups[group] = groups[group]!! + item
    }

    for ((group, items) in groups.entries) {
        item(group) {
            header(group)
        }

        items(items, key = key) { item: T ->
            content(item)
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun NewsPage(
    viewModel: NewsPageViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNewsItemClick: (news: News) -> Unit = {}
) {
    val search by viewModel.search.collectAsState("")

    val news = viewModel.news.collectAsLazyPagingItems()

    LazyColumn() {
        item(key = "search") {

            TextField(
                value = search,
                onValueChange = { viewModel.search.compareAndSet(search, it) },
                label = "Keresés",
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }

        if (news.loadState.refresh is LoadState.Loading) {
            item(key = "refresh_loading") {
                CircularProgressIndicator()
            }
        } else {
            groupedByItems(
                items = news,
                key = { it.id },
                groupBy = { item ->
                    val diffInMillis = Date().time - item.date.time
                    val diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                    when (diff) {
                        0L -> "Ma"
                        1L -> "Tegnap"
                        else -> "$diff napja"
                    }
                },
                header = {
                    Text(it)
                }
            ) { news ->
                NewsCard(
                    news,
                    onClick = { onNewsItemClick(news) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

//            items(news, key = { it.id }) { newsItem ->
//                val news = newsItem ?: return@items
//
//                NewsCard(
//                    news,
//                    onClick = { onNewsItemClick(news) },
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                )
//            }
        }

        if (news.loadState.append is LoadState.Loading) {
            item(key = "append_loading") {
                CircularProgressIndicator()
            }
        }

        nowPlayingPadding()
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun Preview_NewsPage() {
    NewsPage()
}