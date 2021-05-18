package hu.hitgyulekezete.hitradio.audio.metadata

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality

data class Metadata(
    val title: String,
    val subtitle: String?,
    val artUri: String?,
    val type: MetadataType,
) {

    fun asMediaMetadataCompat(): MediaMetadataCompat {
        return MediaMetadataCompat.Builder().run {
            this.putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
            this.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title)
            this.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, subtitle)

            // TODO
//            this.putString(MediaMetadataCompat.METADATA_KEY_DURATION, subtitle)

            if (artUri != null) {
                this.putString(MediaMetadataCompat.METADATA_KEY_ART_URI, subtitle)
                this.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, subtitle)
            }

            build()
        }
    }

    fun asMediaItem(mediaId: String): MediaBrowserCompat.MediaItem {
        return MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder().run {
                setMediaId(mediaId)
                setTitle(title)
                setSubtitle(subtitle)

                if (artUri != null) {
                    setIconUri(Uri.parse(artUri))
                }

                build()
            },
            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE,
        )
    }

    companion object {
        fun from(description: MediaDescriptionCompat?): Metadata {
            if (description == null || description.title == null) {
                return Empty
            }

            return Metadata(
                title = description.title!!.toString(),
                subtitle = description.subtitle?.toString(),
                artUri = description.iconUri?.toString(),
                type = if(description.extras?.get("IS_LIVE") == true) MetadataType.LIVE else MetadataType.NORMAL
            )
        }

        val Empty = Metadata(
            title = "",
            subtitle = null,
            artUri = null,
            type = MetadataType.NORMAL,
        )
    }
}

fun Metadata?.artUriOrDefault(): String {
    return this?.artUri ?: "https://myonlineradio.hu/public/uploads/radio_img/hit-radio/play_250_250.jpg"
}