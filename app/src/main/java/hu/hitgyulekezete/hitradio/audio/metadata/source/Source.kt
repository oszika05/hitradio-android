package hu.hitgyulekezete.hitradio.audio.metadata.source

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository

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

private const val KEY_BUNDLE_SOURCE_TYPE = "KEY_BUNDLE_TYPE"
private const val KEY_BUNDLE_ID = "KEY_BUNDLE_ID"
private const val KEY_BUNDLE_NAME = "KEY_BUNDLE_NAME"
private const val KEY_BUNDLE_DESCRIPTION = "KEY_BUNDLE_DESCRIPTION"
private const val KEY_BUNDLE_METADTA_TITLE = "KEY_BUNDLE_METADTA_TITLE"
private const val KEY_BUNDLE_METADTA_SUBTITLE = "KEY_BUNDLE_METADTA_SUBTITLE"
private const val KEY_BUNDLE_METADTA_ART_URI = "KEY_BUNDLE_METADTA_ART_URI"
private const val KEY_BUNDLE_METADTA_TYPE = "KEY_BUNDLE_METADTA_TYPE"
private const val KEY_BUNDLE_URL_LOW = "KEY_BUNDLE_URL_LOW"
private const val KEY_BUNDLE_URL_MID = "KEY_BUNDLE_URL_MID"
private const val KEY_BUNDLE_URL_HIGH = "KEY_BUNDLE_URL_HIGH"

fun Bundle?.asSource(currentProgramRepository: CurrentProgramRepository): Source? {
    this ?: return null

    val id: String = getString(KEY_BUNDLE_ID) ?: return null
    val name: String = getString(KEY_BUNDLE_NAME) ?: return null
    val description: String? = getString(KEY_BUNDLE_DESCRIPTION)
    val url = SourceUrl(
        low = getString(KEY_BUNDLE_URL_LOW) ?: return null,
        medium = getString(KEY_BUNDLE_URL_MID) ?: return null,
        high = getString(KEY_BUNDLE_URL_HIGH) ?: return null,
    )

    return when (getString(KEY_BUNDLE_SOURCE_TYPE)) {
        "changing" -> ChangingMetadataSource(
            id = id,
            name = name,
            description = description,
            url = url,
            currentProgramRepository = currentProgramRepository
        )
        else -> SimpleSource(
            id = id,
            name = name,
            description = description,
            metadata = Metadata(
                title = getString(KEY_BUNDLE_METADTA_TITLE) ?: return null,
                subtitle = getString(KEY_BUNDLE_METADTA_SUBTITLE),
                artUri = getString(KEY_BUNDLE_METADTA_ART_URI),
                type = getSerializable(KEY_BUNDLE_METADTA_TYPE) as MetadataType ?: return null
            ),
            url = url,
        )
    }
}

fun Source.asBundle(): Bundle {
    val type = when (this) {
        is ChangingMetadataSource -> "changing"
        else -> "simple"
    }

    return Bundle().apply {
        putString(KEY_BUNDLE_SOURCE_TYPE, type)
        putString(KEY_BUNDLE_ID, id)
        putString(KEY_BUNDLE_NAME, name)
        description?.let { description ->
            putString(KEY_BUNDLE_DESCRIPTION, description)
        }

        putString(KEY_BUNDLE_METADTA_TITLE, metadata.title)
        metadata.subtitle?.let { subtitle ->
            putString(KEY_BUNDLE_METADTA_SUBTITLE, subtitle)
        }
        metadata.artUri?.let { artUri ->
            putString(KEY_BUNDLE_METADTA_ART_URI, artUri)
        }
        putSerializable(KEY_BUNDLE_METADTA_TYPE, metadata.type)

        putString(KEY_BUNDLE_URL_LOW, url[StreamQuality.Low])
        putString(KEY_BUNDLE_URL_MID, url[StreamQuality.Medium])
        putString(KEY_BUNDLE_URL_HIGH, url[StreamQuality.High])
    }
}

fun Source?.asMediaSource(streamQuality: StreamQuality): MediaSource? {
    this ?: return null

    return when (metadata.type) {
        MetadataType.NORMAL -> {
            val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory()

            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url[streamQuality]))
        }
        MetadataType.LIVE -> {
            val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory()

            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url[streamQuality]))
        }
    }
}