package hu.hitgyulekezete.hitradio.view.components.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.layout.header.Header
import hu.hitgyulekezete.hitradio.view.components.layout.header.LazyListWithStickyHeader

@Composable
fun PageLayout(
    listState: LazyListState = rememberLazyListState(),
    headerTitle: String,
    isBackEnabled: Boolean = true,
    onBackClick: () -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    LazyListWithStickyHeader(
        scrollState = listState,
        headerContent = {
            Header(
                title = headerTitle,
                onBackClick = onBackClick,
            )
        },
    ) {
        content()
    }
}

@Preview
@Composable
fun Preview_PageLayout() {
    PreviewContainer(disablePadding = true) {
        PageLayout(headerTitle = "Test title") {
            items(100) { i ->
                Text(
                    "alma $i",
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {}
                )
            }
        }
    }
}