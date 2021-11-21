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
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import hu.hitgyulekezete.hitradio.view.layout.primaryText

@Composable
fun GroupHeader(
    group: String,
    modifier: Modifier = Modifier
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
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
    content: @Composable (item: T) -> Unit,
) {
    var lastGroup: G? = null

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
