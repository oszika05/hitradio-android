package hu.hitgyulekezete.hitradio.view.components.news.relatednewscard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import java.util.*

@Composable
fun RelatedNewsCard(
    item: News,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .width(175.dp)
            .height(215.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            Image(
                rememberImagePainter(item.picture),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(1.2f),
            )

            Text(
                item.title,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body2,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun Preview_RelatedNewsCard() {
    PreviewContainer {
        Row(
            Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RelatedNewsCard(
                item = News(
                    id = "1",
                    title = "Test news ".repeat(10),
                    picture = "https://www.netafimindia.com/48d5e8/globalassets/demo/potato/potatoes_challenge.jpg?height=0&width=750&mode=crop&quality=80",
                    date = Date(),
                    tags = listOf(),
                    content = "",
                )
            )
            RelatedNewsCard(
                item = News(
                    id = "2",
                    title = "Kirúghatnak egy amerikai egyetemről, ha rossz névmást használsz",
                    picture = "https://www.netafimindia.com/48d5e8/globalassets/demo/potato/potatoes_challenge.jpg?height=0&width=750&mode=crop&quality=80",
                    date = Date(),
                    tags = listOf(),
                    content = "",
                )
            )
            RelatedNewsCard(
                item = News(
                    id = "3",
                    title = "Kirúghatnak egy amerikai egyetemről, ha rossz névmást használsz",
                    picture = "https://www.netafimindia.com/48d5e8/globalassets/demo/potato/potatoes_challenge.jpg?height=0&width=750&mode=crop&quality=80",
                    date = Date(),
                    tags = listOf(),
                    content = "",
                )
            )
        }
    }
}