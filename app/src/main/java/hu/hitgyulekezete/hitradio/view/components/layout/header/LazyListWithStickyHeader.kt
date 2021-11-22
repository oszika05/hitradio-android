package hu.hitgyulekezete.hitradio.view.components.layout.header

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class StickyHeaderViewModel @Inject constructor() : ViewModel() {
    private var lastScrollIndex = 0

    private val _scrollUp = MutableStateFlow(true)
    val scrollUp: StateFlow<Boolean> = _scrollUp

    private val _lastScroll = MutableStateFlow<Int>(0)
    val lastScroll: StateFlow<Int> = _lastScroll

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex < lastScrollIndex
        _lastScroll.value = newScrollIndex
        lastScrollIndex = newScrollIndex
    }
}

@Composable
fun LazyListWithStickyHeader(
    scrollState: LazyListState,
    viewModel: StickyHeaderViewModel = hiltViewModel(),
    headerContent: @Composable () -> Unit,
    bigHeaderContent: (@Composable () -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    val scrollUp by viewModel.scrollUp.collectAsState(true)
    val lastScroll by viewModel.lastScroll.collectAsState(0)

    viewModel.updateScrollPosition(scrollState.firstVisibleItemIndex)

    val shouldShowMenu = scrollUp && (bigHeaderContent == null || lastScroll > 0)

    val position by animateFloatAsState(
        if (shouldShowMenu) 0f else -300f,
        animationSpec = tween(500)
    )
    val alpha by animateFloatAsState(
        if (shouldShowMenu) 1f else 0f,
        animationSpec = if (shouldShowMenu) {
            tween(500)
        } else {
            tween(200)
        }
    )

    Box() {
        LazyColumn(
            state = scrollState,
        ) {
            item("sticky_header_padding") {
                if (bigHeaderContent != null) {
                    bigHeaderContent()
                } else {
                    Box(Modifier.alpha(0f)) {
                        headerContent()
                    }
                }

            }

            content()
        }

        Box(
            Modifier
                .graphicsLayer {
                    translationY = position
                    this.alpha = alpha
                }
        ) {
            headerContent()
        }
    }

}

@Preview
@Composable
fun Preview_StickyHeader() {
    PreviewContainer(disablePadding = true) {
        val scrollState = rememberLazyListState()

        LazyListWithStickyHeader(scrollState, headerContent = {
            Header("this is a test")
        }) {
            items(100) { i ->
                Text("item $i")
            }
        }


    }
}