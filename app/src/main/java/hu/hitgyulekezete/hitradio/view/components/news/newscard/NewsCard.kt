package hu.hitgyulekezete.hitradio.view.components.news.newscard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.listitem.ListItem
import java.util.*

@Composable
fun NewsCard(
    item: News,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ListItem(
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