package hu.hitgyulekezete.hitradio.view.podcast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram
import hu.hitgyulekezete.hitradio.model.podcast.repository.MockPodcastRepository
import hu.hitgyulekezete.hitradio.model.podcast.repository.PodcastRepository
import hu.hitgyulekezete.hitradio.model.program.Program

@Composable
fun PodcastProgramListItem(
    program: PodcastProgram,
) {
    Row(
        Modifier
            .height(56.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberImagePainter(program.artUriOrDefault(),),
            contentDescription = "album image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(
                    RoundedCornerShape(4.dp)
                )
        )

        Text(
            program.name,
            fontSize = 18.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun PodcastProgramListItemPreview() {
    PodcastProgramListItem(
        program = PodcastProgram.test1,
    )
}