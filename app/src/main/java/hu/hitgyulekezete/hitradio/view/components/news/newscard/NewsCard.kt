package hu.hitgyulekezete.hitradio.view.components.news.newscard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.card.Card
import hu.hitgyulekezete.hitradio.view.components.skeleton.skeleton
import java.util.*

@Composable
fun NewsCardSkeleton(
    modifier: Modifier = Modifier,
) {
    NewsCard(
        item = News(
            id = "",
            title = "",
            picture = "",
            date = Date(),
            tags = listOf(),
            content = "",
        ),
        modifier = modifier
            .skeleton()
    )

}

@Composable
fun NewsCard(
    item: News,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier,
        image = rememberImagePainter(item.picture),
        contentDescription = item.title,
        title = item.title,
        onClick = onClick,
    )
}

@Preview
@Composable
fun Preview_NewsCard() {
    PreviewContainer {
        NewsCard(
            item = News(
                id = "1",
                title = "Test news ".repeat(4),
                picture = "https://www.netafimindia.com/48d5e8/globalassets/demo/potato/potatoes_challenge.jpg?height=0&width=750&mode=crop&quality=80",
                date = Date(),
                tags = listOf(),
                content = "",
            )
        )
    }
}