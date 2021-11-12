package hu.hitgyulekezete.hitradio.view.components.episode.episodecard

import android.media.session.PlaybackState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.PersonType
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.view.PlayPauseButton
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import java.util.*

@Composable
fun EpisodeCard(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    playbackState: AudioStateManager.PlaybackState,
    onPlayClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                rememberImagePainter(episode.program.pictureOrDefault),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(25))
            )

            PlayPauseButton(playbackState = playbackState) {
                onPlayClick()
            }
        }

        Column(
            Modifier
                .fillMaxHeight()
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                episode.title,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                episode.program.name,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}

@Preview
@Composable
fun Preview_EpisodeCard() {
    PreviewContainer {
        EpisodeCard(
            episode = Episode(
                id = "1",
                title = "test ".repeat(5),
                date = Date(),
                description = "blah blah ".repeat(25),
                tags = listOf("tag1", "tag2"),
                program = Program(
                    id = "1",
                    name = "test tesst",
                    picture = "https://upload.wikimedia.org/wikipedia/commons/c/cf/Pears.jpg",
                    description = "bbla ".repeat(50),
                ),
                audioUrl = "",
                hosts = listOf(
                    Person(
                        id = "1",
                        name = "test 1",
                        type = PersonType.Host,
                        picture = null,
                        description = null,
                    )
                ),
                guests = listOf(
                    Person(
                        id = "2",
                        name = "test 2",
                        type = PersonType.Guest,
                        picture = null,
                        description = null,
                    )
                )
            ),
            playbackState = AudioStateManager.PlaybackState.STOPPED,
        )
    }
}