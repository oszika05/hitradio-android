package hu.hitgyulekezete.hitradio.audio

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.media.MediaDescription
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.offline.DownloadIndex
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService

class AudioController(
    private val activity: Activity
) {

    private var source: Source? = null
    private var mediaBrowser: MediaBrowserCompat? = null
    private var callbackRegistered: Boolean = false

    private val _currentMediaId = MutableLiveData<String?>(null)
    val mediaId: LiveData<String?> = _currentMediaId

    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            val token = mediaBrowser!!.sessionToken

            // Create a MediaControllerCompat
            val mediaController = MediaControllerCompat(
                activity,
                token
            )

            MediaControllerCompat.setMediaController(activity, mediaController)
        }

        override fun onConnectionSuspended() {
            // The Service has crashed. Disable transport controls until it automatically reconnects
        }

        override fun onConnectionFailed() {
            // The Service has refused our connection
        }
    }

    private val mediaController: MediaControllerCompat by lazy {
        val controller = MediaControllerCompat.getMediaController(activity)

        if (!callbackRegistered) {
            controller.registerCallback(controllerCallback)
            callbackRegistered = true
        }

        return@lazy controller
    }

    private val _metadata = MutableLiveData<Metadata>()
    val metadata: LiveData<Metadata> = _metadata

    private val _playbackState = MutableLiveData<PlaybackState>(PlaybackState.STOPPED)
    var playbackState: LiveData<PlaybackState> = _playbackState

    private fun createMediaBrowser(): MediaBrowserCompat {
        return MediaBrowserCompat(
            activity,
            ComponentName(activity, MediaPlaybackService::class.java),
            connectionCallbacks,
            null // optional Bundle
        )
    }

    private fun connectIfNeeded() {
        if (mediaBrowser == null || !mediaBrowser!!.isConnected) {
            mediaBrowser = createMediaBrowser()
            Log.d("CONTROLLER", "connect to mediaBrowser..")
            mediaBrowser!!.connect()
        }
    }

    fun setSource(source: Source) {
        connectIfNeeded()

        this.source = source

        val quality = StreamQuality.High // TODO

//        // TODO
//        if (mediaController.queue != null) {
//            mediaController.queue.removeAll { true }
//        }

        mediaController.transportControls.prepareFromMediaId(source.id, Bundle.EMPTY)
//        mediaController.transportControls.playFromMediaId(source.id, Bundle.EMPTY)
//        mediaController.addQueueItem(source.asMediaDescriptionCompat(quality), 0)
    }

    fun playPause() {
        connectIfNeeded()

        Log.d(
            "CLEINT",
            "state: ${playbackState.value}, playing: ${playbackState.value == PlaybackState.PLAYING || playbackState.value == PlaybackState.BUFFERING}"
        )

        if (playbackState.value == PlaybackState.PLAYING || playbackState.value == PlaybackState.BUFFERING) {
            mediaController.transportControls.pause()
        } else {
            mediaController.transportControls.play()
        }
    }

    fun downloadContent(source: Source) {
        if (source.metadata.type != MetadataType.NORMAL) {
            return
        }
        val downloadRequest: DownloadRequest = DownloadRequest.Builder(
            source.id,
            Uri.parse(source.url[StreamQuality.High])
        ).build()

        DownloadService.sendAddDownload(
            activity,
            hu.hitgyulekezete.hitradio.audio.service.DownloadService::class.java,
            downloadRequest,
            false
        )
    }

    fun isDownloaded(source: Source): Boolean {
        val index = hu.hitgyulekezete.hitradio.audio.service.DownloadService.getDownloadManager(activity).downloadIndex

        return try {
            index.getDownload(source.id) != null
        } catch (e: Exception) {
            false
        }
    }

    fun removeDownload(source: Source) {
        DownloadService.sendRemoveDownload(
            activity,
            hu.hitgyulekezete.hitradio.audio.service.DownloadService::class.java,
            source.id,
            false
        )
    }

    fun connect() {
        mediaBrowser = createMediaBrowser()

        connectIfNeeded()
    }

    fun disconnect() {
        MediaControllerCompat.getMediaController(activity)?.unregisterCallback(controllerCallback)
        callbackRegistered = false
        mediaBrowser?.disconnect()
        mediaBrowser = null
    }

    private val controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            Log.d("CONTROLLER", "onMetadataChanged: title: ${metadata?.description?.title}")
            _currentMediaId.postValue(metadata?.description?.mediaId)
            _metadata.postValue(
                Metadata.from(metadata?.description)
            )
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            Log.d("CONTROLLER", "onPlaybackStateChanged: ${state?.state}")
            _playbackState.postValue(PlaybackState.fromPlaybackStateCompat(state?.state))
        }
    }


    enum class PlaybackState {
        PLAYING,
        PAUSED,
        BUFFERING,
        STOPPED;

        fun toggle(): AudioController.PlaybackState {
            return if (this == PLAYING || this == BUFFERING) {
                STOPPED
            } else {
                PLAYING
            }
        }

        fun isPlaying(): Boolean {
            return this == PLAYING || this == BUFFERING
        }

        companion object {
            fun fromPlaybackStateCompat(state: Int?): PlaybackState {
                return when (state) {
                    PlaybackStateCompat.STATE_BUFFERING -> BUFFERING
                    PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.STATE_ERROR -> STOPPED
                    PlaybackStateCompat.STATE_PAUSED -> PAUSED
                    PlaybackStateCompat.STATE_PLAYING -> PLAYING
                    else -> STOPPED
                }
            }
        }
    }
}
