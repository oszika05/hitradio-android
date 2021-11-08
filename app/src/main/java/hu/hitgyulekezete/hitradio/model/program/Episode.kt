package hu.hitgyulekezete.hitradio.model.program

import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.SimpleSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import java.util.*

data class Episode(
    val id: String,
    val title: String,
    val date: Date,
    val description: String?,
    val tags: List<String>,
    val program: Program,
    val audioUrl: String,
    val hosts: List<Person>,
    val guests: List<Person>,
)

fun Episode.asSource(): Source {
    return SimpleSource(
        id = id,
        name = title,
        description = program.name,
        metadata = hu.hitgyulekezete.hitradio.audio.metadata.Metadata(
            title = title,
            subtitle = program.name,
            artUri = program.pictureOrDefault,
            type = MetadataType.NORMAL,
        ),
        url = SourceUrl(url = audioUrl),
    )
}
