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
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
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
import hu.hitgyulekezete.hitradio.audio.metadata.source.asBundle
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.toLongOrDefault
import kotlin.coroutines.CoroutineContext

class AudioController(
    private val activity: Activity
) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


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

    private val playbackStateFlow = MutableStateFlow(AudioStateManager.PlaybackState.STOPPED)
    val playbackState: StateFlow<AudioStateManager.PlaybackState> = playbackStateFlow

    private val metadataFlow = MutableStateFlow<Metadata>(Metadata.from(null))
    val metadata: StateFlow<Metadata> = metadataFlow

    private val seekPositionFlow = MutableStateFlow<Float?>(null)
    val seekPosition: StateFlow<Float?> = seekPositionFlow

    fun seekTo(percentage: Float) {
        seekPositionManager.seekTo(percentage)
    }

    fun playPause() {
        stateManager.playPause()
    }

    private val _currentMediaId = MutableStateFlow<String?>(null)
    val mediaId: StateFlow<String?> = _currentMediaId

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

            launch {
                metadataManager.metadata.collect {
                    metadataFlow.value = it
                }
            }

            launch {
                seekPositionManager.seekPosition.collect {
                    seekPositionFlow.value = it
                }
            }

            launch {
                stateManager.playbackState.collect {
                    playbackStateFlow.value = it
                }
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

        if (source.url[quality].isNotBlank()) {
            mediaController.transportControls.prepareFromUri(
                source.url[quality].toUri(),
                source.asBundle()
            )
        }

//        mediaController.transportControls.prepareFromMediaId(source.id, Bundle.EMPTY)
//        mediaController.transportControls.playFromMediaId(source.id, Bundle.EMPTY)
//        mediaController.addQueueItem(source.asMediaDescriptionCompat(quality), 0)
    }

    fun connect() {
        job.start()

        mediaBrowser = createMediaBrowser()
        connectIfNeeded()
    }

    fun disconnect() {
        job.cancel()

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
            _currentMediaId.value = metadata?.description?.mediaId
        }
    }
}

fun AudioController.sourcePlaybackState(source: Source?): Flow<AudioStateManager.PlaybackState> =
    flow {
        playbackState
            .combine(mediaId) { playbackState, mediaId -> playbackState to mediaId }
            .collect { (playbackState, mediaId) ->
                if (source?.url?.get(StreamQuality.High)?.isBlank() == true) {
                    emit(AudioStateManager.PlaybackState.LOCKED)
                } else if (source?.id == null) {
                    emit(AudioStateManager.PlaybackState.STOPPED)
                } else if (mediaId != source?.id) {
                    emit(AudioStateManager.PlaybackState.STOPPED)
                } else {
                    emit(playbackState)
                }
            }
    }

fun AudioController.sourcePlaybackStateById(id: String?): Flow<AudioStateManager.PlaybackState> =
    flow {
        playbackState
            .combine(mediaId) { playbackState, mediaId -> playbackState to mediaId }
            .collect { (playbackState, mediaId) ->
               if (id == null) {
                    emit(AudioStateManager.PlaybackState.STOPPED)
                } else if (mediaId != id) {
                    emit(AudioStateManager.PlaybackState.STOPPED)
                } else {
                    emit(playbackState)
                }
            }
    }

fun AudioController.playPauseForSource(source: Source) {
    if (mediaId.value == source.id) {
        playPause()
    } else {
        val needToPlay = playbackState.value != AudioStateManager.PlaybackState.BUFFERING && playbackState.value != AudioStateManager.PlaybackState.PLAYING
        setSource(source)

        if (needToPlay) {
            playPause()
        }
    }
}
