package hu.hitgyulekezete.hitradio.view.components.layout.header

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

    private val _scrollUp = MutableStateFlow(false)
    val scrollUp: StateFlow<Boolean> = _scrollUp

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }
}

@Composable
fun LazyListWithStickyHeader(
    scrollState: LazyListState,
    viewModel: StickyHeaderViewModel = hiltViewModel(),
    headerContent: @Composable () -> Unit,
    content: LazyListScope.() -> Unit,
) {
    val scrollUpState by viewModel.scrollUp.collectAsState(false)

    viewModel.updateScrollPosition(scrollState.firstVisibleItemIndex)

    val position by animateFloatAsState(
        if (scrollUpState) -300f else 0f,
        animationSpec = tween(500)
    )
    val alpha by animateFloatAsState(
        if (scrollUpState) 0f else 1f,
        animationSpec = if (scrollUpState) {
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
                Box(Modifier.alpha(0f)) {
                    headerContent()
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
//            Text("This is a test")
            Header("this is a test")
        }) {
            items(100) { i ->
                Text("item $i")
            }
        }


    }
}