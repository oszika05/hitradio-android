package hu.hitgyulekezete.hitradio.audio.metadata.source

import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl

class SimpleSource(
    override val id: String,
    override val name: String,
    override val description: String,
    override val metadata: Metadata,
    override val url: SourceUrl
) : Source {

    override fun observe(observer: Source.MetadataObserver) {
        observer.onMetadataChanged(metadata)
    }

    override fun removeObserver(observer: Source.MetadataObserver) {
        // do nothing, since we do not store the observer
    }
}