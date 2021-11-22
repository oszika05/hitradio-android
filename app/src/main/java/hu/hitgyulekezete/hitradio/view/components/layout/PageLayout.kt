package hu.hitgyulekezete.hitradio.view.components.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.layout.header.Header
import hu.hitgyulekezete.hitradio.view.components.layout.header.LazyListWithStickyHeader

@Composable
fun PageLayout(
    listState: LazyListState = rememberLazyListState(),
    headerTitle: String,
    headerImage: Painter? = null,
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
        bigHeaderContent = headerImage?.let { headerImage ->
            {
                Box() {
                    Image(
                        headerImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.77f)
                    )

                    Header(
                        title = "",
                        isTransparent = true,
                        onBackClick = onBackClick,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

            }
        }
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