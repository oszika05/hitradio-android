package hu.hitgyulekezete.hitradio.model.podcast

import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.SimpleSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl

data class PodcastProgram(
    val id: String,
    val name: String,
    val description: String,
    val artUri: String?,
    val podcasts: List<Source>
) {
    fun artUriOrDefault(): String {
        return artUri ?: DEFAULT_ART_URI
    }

    companion object {
        val test1 = PodcastProgram(
            id = "pod-1",
            name = "Test podcast program #1",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam sollicitudin, arcu eget venenatis ullamcorper, ipsum sem suscipit diam, eu eleifend ligula ex ut turpis. Pellentesque sit amet lacinia lorem. Aenean aliquet elit vitae vulputate dignissim. Vivamus fringilla consequat dictum. Aliquam eget leo lorem. Nunc pretium hendrerit nisi eu ultrices. In.",
            artUri = "https://ugc.futurelearn.com/uploads/images/a8/34/a834945b-ea3e-4ef1-8f77-8230e8fd403d.jpg",
            podcasts = listOf<Source>(
                SimpleSource(
                    id = "1",
                    name = "Teszt podcast",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt rutrum mi ut congue. Fusce pellentesque venenatis placerat. Phasellus hendrerit nisi est, sit amet feugiat.",
                    metadata = Metadata(
                        title = "Teszt podcast",
                        subtitle = "Test podcast program #1",
                        artUri = "https://ugc.futurelearn.com/uploads/images/a8/34/a834945b-ea3e-4ef1-8f77-8230e8fd403d.jpg",
                        type = MetadataType.NORMAL,
                    ),
                    url = SourceUrl(url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3")
                ),
                SimpleSource(
                    id = "2",
                    name = "Teszt podcast 2",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt rutrum mi ut congue. Fusce pellentesque venenatis placerat. Phasellus hendrerit nisi est, sit amet feugiat.",
                    metadata = Metadata(
                        title = "Teszt podcast 2",
                        subtitle = "Test podcast program #1",
                        artUri = "https://ugc.futurelearn.com/uploads/images/a8/34/a834945b-ea3e-4ef1-8f77-8230e8fd403d.jpg",
                        type = MetadataType.NORMAL,
                    ),
                    url = SourceUrl(url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3")
                )
            )
        )

        val test2 = PodcastProgram(
            id = "pod-2",
            name = "Test podcast program #2",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam sollicitudin, arcu eget venenatis ullamcorper, ipsum sem suscipit diam, eu eleifend ligula ex ut turpis. Pellentesque sit amet lacinia lorem. Aenean aliquet elit vitae vulputate dignissim. Vivamus fringilla consequat dictum. Aliquam eget leo lorem. Nunc pretium hendrerit nisi eu ultrices. In.",
            artUri = "https://influencermarketing.ai/wp-content/uploads/2020/05/Podcast-blog-6.jpg",
            podcasts = listOf<Source>(
                SimpleSource(
                    id = "2-1",
                    name = "Teszt podcast 1",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt rutrum mi ut congue. Fusce pellentesque venenatis placerat. Phasellus hendrerit nisi est, sit amet feugiat.",
                    metadata = Metadata(
                        title = "Teszt podcast",
                        subtitle = "Test podcast program #2",
                        artUri = "https://influencermarketing.ai/wp-content/uploads/2020/05/Podcast-blog-6.jpg",
                        type = MetadataType.NORMAL,
                    ),
                    url = SourceUrl(url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3")
                ),
                SimpleSource(
                    id = "2-2",
                    name = "Teszt podcast 2",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt rutrum mi ut congue. Fusce pellentesque venenatis placerat. Phasellus hendrerit nisi est, sit amet feugiat.",
                    metadata = Metadata(
                        title = "Teszt podcast 2",
                        subtitle = "Test podcast program #2",
                        artUri = "https://influencermarketing.ai/wp-content/uploads/2020/05/Podcast-blog-6.jpg",
                        type = MetadataType.NORMAL,
                    ),
                    url = SourceUrl(url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3")
                )
            )
        )

        const val DEFAULT_ART_URI = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg"
    }


}