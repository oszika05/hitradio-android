package hu.hitgyulekezete.hitradio.view.pages.newsitem

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.hitgyulekezete.hitradio.model.news.News
import java.util.*

@Composable
fun NewsItemPage(
    news: News
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .verticalScroll(scrollState)
    ) {
        Text(news.title, Modifier.padding(bottom = 32.dp), style = MaterialTheme.typography.h4)
        Text(news.content, style = MaterialTheme.typography.body1)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_NewsItemPage() {
    val news = News(
        id = "test",
        title = "Kimondani is hihetetlen: Zuckerberg 2 ezer milliárd forintnyi összeget bukott néhány óra alatt",
        content = "O, belay. The biscuit eater tastes with hunger, view the captain's quarters before it stutters. Sunny, weird hornpipes cowardly fight a shiny, undead biscuit eater. The shipmate burns with courage, raid the brig. Ah! Pieces o' adventure are forever warm. Gar, ye mighty plank- set sails for riddle! Never haul a son. How coal-black. You loot like a codfish. Cockroach of a shiny life, raid the hunger! The captain hoists with horror, taste the cook islands until it grows. The cannon pulls with greed, endure the captain's quarters. The pirate endures with fortune, blow the galley until it dies!",
        picture = "https://upload.wikimedia.org/wikipedia/commons/c/cf/Pears.jpg",
        date = Date(),
        tags = listOf("facebook", "penz")
    )

    NewsItemPage(news = news)
}