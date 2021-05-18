package hu.hitgyulekezete.hitradio.audio.metadata.source

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService

interface Source {
    val id: String
    val name: String
    val description: String?
    val metadata: Metadata
    val url: SourceUrl

    fun observe(observer: MetadataObserver)
    fun removeObserver(observer: MetadataObserver)

    interface MetadataObserver {
        fun onMetadataChanged(metadata: Metadata)
    }

    fun asMediaDescriptionCompat(streamQuality: StreamQuality = StreamQuality.High): MediaDescriptionCompat {
        return MediaDescriptionCompat.Builder().run {

            this.setTitle(metadata.title)
            this.setSubtitle(metadata.subtitle)
            if (metadata.artUri != null) {
                this.setIconUri(Uri.parse(metadata.artUri!!))
            }

            this.setMediaId(id)
            this.setMediaUri(Uri.parse(url[streamQuality]))

            build()
        }
    }

}