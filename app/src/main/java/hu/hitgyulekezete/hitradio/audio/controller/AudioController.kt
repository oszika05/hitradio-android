package hu.hitgyulekezete.hitradio.audio.controller

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.media.MediaDescription
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.offline.DownloadIndex
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import hu.hitgyulekezete.hitradio.audio.controller.seekposition.SeekPositionManager
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService
import okhttp3.internal.toLongOrDefault

class AudioController(
    private val activity: Activity
) {
    private var source: Source? = null
    private var mediaBrowser: MediaBrowserCompat? = null
    private var callbackRegistered: Boolean = false

    private val handler = Handler(activity.mainLooper)

    private val stateManager: AudioStateManager by lazy {
        AudioStateManager(mediaController)
    }

    private val metadataManager: MetadataManager by lazy {
        MetadataManager(mediaController)
    }

    private val seekPositionManager: SeekPositionManager by lazy {
        SeekPositionManager(mediaController, handler)
    }

    private val playbackStateMediator = MediatorLiveData<AudioStateManager.PlaybackState>()
    val playbackState: LiveData<AudioStateManager.PlaybackState> by lazy {
        playbackStateMediator
    }

    private val metadataMediator = MediatorLiveData<Metadata>()
    val metadata: LiveData<Metadata> by lazy {
        metadataMediator
    }

    private val seekPositionMediator = MediatorLiveData<Float?>()
    val seekPosition: LiveData<Float?> by lazy {
        seekPositionMediator
    }

    fun seekTo(percentage: Float) {
        seekPositionManager.seekTo(percentage)
    }

    fun playPause() {
        stateManager.playPause()
    }

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

            metadataManager.connect()
            seekPositionManager.start()
            stateManager.connect()

            metadataMediator.removeSource(metadataManager.metadata)
            metadataMediator.addSource(metadataManager.metadata) {
                metadataMediator.value = it
            }

            seekPositionMediator.removeSource(seekPositionManager.seekPosition)
            seekPositionMediator.addSource(seekPositionManager.seekPosition) {
                seekPositionMediator.value = it
            }

            playbackStateMediator.removeSource(stateManager.playbackState)
            playbackStateMediator.addSource(stateManager.playbackState) {
                playbackStateMediator.value = it
            }
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
            // TODO
            callbackRegistered = true
        }

        return@lazy controller
    }


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

    fun connect() {
        mediaBrowser = createMediaBrowser()
        connectIfNeeded()
    }

    fun disconnect() {
        metadataManager.disconnect()
        seekPositionManager.stop()
        stateManager.disconnect()

        MediaControllerCompat.getMediaController(activity)?.unregisterCallback(controllerCallback)

        callbackRegistered = false
        mediaBrowser?.disconnect()
        mediaBrowser = null

    }

    private val controllerCallback = object : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _currentMediaId.postValue(metadata?.description?.mediaId)
        }
    }
}
