package hu.hitgyulekezete.hitradio.audio.service

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.ChangingMetadataSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.LIVE_ID
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.audio.metadata.source.live
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService.Companion.MEDIA_ROOT_ID
import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem
import hu.hitgyulekezete.hitradio.model.programguide.api.ProgramGuideApi
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class SourceMediaItem(
    val id: String,
    val name: String,
    val children: List<SourceMediaItem>,
    val source: Source?
) {
    fun asMediaItem(streamQuality: StreamQuality): MediaBrowserCompat.MediaItem {
        return MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder().run {
                setMediaId(id)
                if (source != null) {
                    setTitle(source.metadata.title)
                    setSubtitle(source.metadata.subtitle)

                    setMediaUri(Uri.parse(source.url[streamQuality]))

                    if (source.metadata.artUri != null) {
                        setIconUri(Uri.parse(source.metadata.artUri))
                    }
                }
                build()
            },
            if (source == null) {
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
            } else {
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            }
        )
    }

    fun flattenChildren(): List<SourceMediaItem> {
        return this.children.flatMap { it.flatten() }
    }

    fun flatten(): List<SourceMediaItem> {
        return listOf(this) + flattenChildren()
    }

    fun findById(id: String): SourceMediaItem? {
        if (this.id == id) {
            return this
        }

        for (child in children) {
            val found = child.findById(id)

            if (found != null) {
                return found
            }
        }

        return null
    }

    companion object {
        private val currentProgramRepository = CurrentProgramRepository(listOf())

        private val live: SourceMediaItem = SourceMediaItem(
            id = ChangingMetadataSource.LIVE_ID,
            name = "??l?? ad??s",
            children = listOf(),
            source = ChangingMetadataSource.live(currentProgramRepository),
        )

        private var programs: List<ProgramGuideItem> = listOf()

        fun root(children: List<SourceMediaItem>): SourceMediaItem {
            if (programs.isEmpty()) {
                val programApi = ProgramGuideApi("https://www.hitradio.hu/api/musor_ios.php")
                GlobalScope.launch {
                    programs = programApi.get()
                    currentProgramRepository.setPrograms(programs)
                }
            }

            return SourceMediaItem(
                id = MEDIA_ROOT_ID,
                name = "Hitr??di??",
                children = listOf(live) + children,
                source = null,
            )
        }
    }
}