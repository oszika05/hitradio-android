package hu.hitgyulekezete.hitradio.view.components.episode.episodecard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import hu.hitgyulekezete.hitradio.audio.controller.AudioStateManager
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.PersonType
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.view.common.preview.PreviewContainer
import hu.hitgyulekezete.hitradio.view.components.card.Card
import hu.hitgyulekezete.hitradio.view.components.card.SmallCard
import hu.hitgyulekezete.hitradio.view.components.skeleton.skeleton
import java.util.*

@Composable
fun SmallEpisodeCardSkeleton(
    modifier: Modifier = Modifier,
) {
    SmallEpisodeCard(
        episode = Episode(
            id = "",
            title = "",
            date = Date(),
            description = null,
            tags = listOf(),
            program = Program(
                id = "",
                name = "",
                description = null,
                picture = null,
            ),
            audioUrl = "",
            hosts = listOf(),
            guests = listOf(),
        ),
        playbackState = AudioStateManager.PlaybackState.STOPPED,
        modifier = modifier.skeleton()
    )
}

@Composable
fun SmallEpisodeCard(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    playbackState: AudioStateManager.PlaybackState,
    onPlayClick: () -> Unit = {},
) {
    SmallCard(
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
fun Preview_SmallEpisodeCard() {
    PreviewContainer {
        SmallEpisodeCard(
            modifier = Modifier.fillMaxWidth(),
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