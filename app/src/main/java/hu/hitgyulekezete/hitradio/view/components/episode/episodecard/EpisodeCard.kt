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
import hu.hitgyulekezete.hitradio.view.components.listitem.ListItem
import java.util.*

@Composable
fun EpisodeCard(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    playbackState: AudioStateManager.PlaybackState,
    onPlayClick: () -> Unit = {},
) {
    ListItem(
        modifier = modifier,
        image = rememberImagePainter(episode.program.pictureOrDefault),
        title = episode.title,
        subtitle = episode.program.name,
        playbackState = playbackState,
        onPlayPauseClick = onPlayClick,
        onClick = onClick
    )
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