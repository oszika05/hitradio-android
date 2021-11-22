package hu.hitgyulekezete.hitradio.view.components.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import hu.hitgyulekezete.hitradio.view.components.skeleton.skeleton
import hu.hitgyulekezete.hitradio.view.layout.primaryText

@Composable
fun GroupHeaderSkeleton(
    modifier: Modifier = Modifier,
    autoPadding: Boolean = true,
) {
    GroupHeader(
        "betuk",
        modifier = modifier.skeleton(),
        autoPadding = autoPadding
    )
}


@Composable
fun GroupHeader(
    group: String,
    modifier: Modifier = Modifier,
    autoPadding: Boolean = true,
    noBackground: Boolean = false,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(if (noBackground) Color.Transparent else MaterialTheme.colors.background)
            .padding(if (autoPadding) 16.dp else 0.dp)
            .then(modifier)
    ) {
        Text(
            group,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primaryText,
        )
    }
}

@ExperimentalFoundationApi
fun <T : Any, G> LazyListScope.groupedByItems(
    items: LazyPagingItems<T>,
    key: (item: T) -> String,
    groupBy: (item: T) -> G,
    groupKey: (group: G) -> String,
    header: @Composable (group: G) -> Unit,
    skeleton: (@Composable () -> Unit)? = null,
    headerSkeleton: (@Composable () -> Unit)? = null,
    content: @Composable (item: T) -> Unit,
) {
    var lastGroup: G? = null

    if (items.loadState.refresh is LoadState.Loading && skeleton != null && headerSkeleton != null) {
        item("skeleton-header-1") { headerSkeleton() }
        item("skeleton-1") { skeleton() }
        item("skeleton-2") { skeleton() }
        item("skeleton-header-2") { headerSkeleton() }
        item("skeleton-3") { skeleton() }
        item("skeleton-4") { skeleton() }
        item("skeleton-header-3") { headerSkeleton() }
        item("skeleton-5") { skeleton() }
        item("skeleton-6") { skeleton() }
        item("skeleton-header-4") { headerSkeleton() }
        item("skeleton-7") { skeleton() }
        item("skeleton-8") { skeleton() }
    }

    for (index in 0 until items.itemCount) {
        val item = items.peek(index) ?: continue

        val currentGroup = groupBy(item)

        if (currentGroup != lastGroup) {
//            stickyHeader { header(currentGroup) }
            item { header(currentGroup) }
        }

        item(key(item)) {
            // triggers load
            val currentItem = items[index] ?: return@item

            content(currentItem)
        }

        lastGroup = currentGroup
    }
}
