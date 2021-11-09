package hu.hitgyulekezete.hitradio.audio.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality

import com.google.android.exoplayer2.ui.PlayerNotificationManager
import hu.hitgyulekezete.hitradio.MainActivity
import hu.hitgyulekezete.hitradio.R
import android.graphics.BitmapFactory
import com.google.android.exoplayer2.source.MediaSource
import hu.hitgyulekezete.hitradio.audio.metadata.artUriOrDefault
import hu.hitgyulekezete.hitradio.audio.metadata.source.*
import hu.hitgyulekezete.hitradio.model.podcast.PodcastProgram
import hu.hitgyulekezete.hitradio.model.programguide.api.ProgramGuideApi
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext


class MediaPlaybackService : MediaBrowserServiceCompat(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private val player: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(baseContext).run {
            val attributes = com.google.android.exoplayer2.audio.AudioAttributes.Builder().run {
                build()
            }

            setAudioAttributes(attributes, true)
            setHandleAudioBecomingNoisy(true)
            build()
        }
    }
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var playerNotificationManager: PlayerNotificationManager

    private var observer: Source.MetadataObserver? = null
    private var source: Source? = null
    private var currentMetadata: Metadata? = null
    private var currentMediaId: String? = null // TODO load default ??

    private var streamQuality = StreamQuality.High

    private var currentProgramRepository: CurrentProgramRepository? = null

    override fun onCreate() {
        super.onCreate()
        job.start()

        launch {
            val programGuideApi = ProgramGuideApi("https://www.hitradio.hu/api/musor_ios.php")
            val programs = programGuideApi.get()
            currentProgramRepository = CurrentProgramRepository(programs)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        // Create a MediaSessionCompat
        mediaSession = MediaSessionCompat(baseContext, LOG_TAG).apply {

            // Enable callbacks from MediaButtons and TransportControls
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        or MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
                        or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())

            // MySessionCallback() has methods that handle callbacks from a media controller
//            setCallback(MySessionCallback())

            // Set the session's token so that client activities can communicate with it.
            setSessionToken(sessionToken)
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession!!).apply {

            setPlayer(player)

            this.setPlaybackPreparer(object : MediaSessionConnector.PlaybackPreparer {
                override fun onCommand(
                    player: Player,
                    controlDispatcher: ControlDispatcher,
                    command: String,
                    extras: Bundle?,
                    cb: ResultReceiver?
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun getSupportedPrepareActions(): Long {
                    return PlaybackStateCompat.ACTION_PAUSE or
                            PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                            PlaybackStateCompat.ACTION_PREPARE_FROM_URI or
                            PlaybackStateCompat.ACTION_STOP
                }

                override fun onPrepare(playWhenReady: Boolean) {
                    player.prepare()
                }

                override fun onPrepareFromMediaId(
                    mediaId: String,
                    playWhenReady: Boolean,
                    extras: Bundle?
                ) {
                    currentMediaId = mediaId

                    val root = getItems()
                    val media = root.findById(mediaId)
                    val source = media?.source.asMediaSource(streamQuality) ?: return

                    subscribeToMetadata(media?.source)

                    player.setMediaSource(source)
                    player.prepare()

                    if (playWhenReady) {
                        player.play()
                    }
                }

                override fun onPrepareFromSearch(
                    query: String,
                    playWhenReady: Boolean,
                    extras: Bundle?
                ) {
                    player.prepare()
                }

                override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) {
                    // maybe this would be better
                    // metadata in extras

                    currentProgramRepository ?: return

                    val source = extras.asSource(currentProgramRepository!!) ?: return

                    currentMediaId = source.id

                    subscribeToMetadata(source)

                    player.setMediaSource(source.asMediaSource(streamQuality) ?: return)

                    player.prepare()

                    if (playWhenReady) {
                        player.play()
                    }
                }

            })

            this.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {

                override fun getMediaDescription(
                    player: Player,
                    windowIndex: Int
                ): MediaDescriptionCompat {
                    if (currentMediaId == null) {
                        return MediaDescriptionCompat.Builder().build()
                    }

                    return currentMetadata?.asMediaItem(currentMediaId!!)?.description
                        ?: MediaDescriptionCompat.Builder().build()
                }

            })
        }

        val adapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return currentMetadata?.title ?: ""
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val intent = Intent(this@MediaPlaybackService, MainActivity::class.java)
                return PendingIntent.getActivity(this@MediaPlaybackService, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return currentMetadata?.subtitle
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                val artUri = currentMetadata.artUriOrDefault()

                GlobalScope.launch(Dispatchers.IO) {
                    val bitmap = try {
                        val url = URL(artUri)
                        val connection: HttpURLConnection =
                            url.openConnection() as HttpURLConnection
                        connection.doInput = true
                        connection.connect()
                        val input: InputStream = connection.inputStream
                        BitmapFactory.decodeStream(input)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        null
                    } ?: return@launch

                    withContext(Dispatchers.Main) {
                        callback.onBitmap(bitmap)
                    }
                }

                return null
            }

        }

        playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        ).run {
            setMediaDescriptionAdapter(adapter)
            build()
        }

        playerNotificationManager.setPlayer(player)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun subscribeToMetadata(item: Source?) {
        if (item == source) {
            return
        }

        currentMetadata = item?.metadata

        if (observer != null) {
            source?.removeObserver(observer!!)
        }

        if (item == null) {
            return
        }

        observer = object : Source.MetadataObserver {
            override fun onMetadataChanged(metadata: Metadata) {
                Handler(player.applicationLooper).post {
                    if (currentMetadata == metadata) {
                        return@post
                    }

                    currentMetadata = metadata
                    mediaSessionConnector.invalidateMediaSessionMetadata()
                }
            }
        }
        item.observe(observer!!)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    private fun getItems(): SourceMediaItem {

        // TODO get podcasts and other content
        // TODO also cache

        val podcasts = listOf(PodcastProgram.test1, PodcastProgram.test2).map {
            SourceMediaItem(
                id = it.id,
                name = it.name,
                source = null,
                children = it.podcasts.map { source ->
                    SourceMediaItem(
                        id = source.id,
                        name = source.name,
                        source = source,
                        children = listOf(),
                    )
                }
            )
        }

        return SourceMediaItem.root(children = podcasts)
    }

    override fun onLoadItem(itemId: String?, result: Result<MediaBrowserCompat.MediaItem>) {
        val root = getItems()

        val element = root.findById(itemId ?: ChangingMetadataSource.LIVE_ID)

        result.sendResult(element?.asMediaItem(streamQuality))
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        // Assume for example that the music catalog is already loaded/cached.

        // TODO get stream quality
        // TODO also invalidate, when it changes


        val rootSource = getItems()

        val parent = rootSource.findById(parentId)

        if (parent == null) {
            result.sendResult(listOf())
            return
        }

        val mediaItems = parent.flattenChildren().map { it.asMediaItem(streamQuality) }

        result.sendResult(mediaItems)
    }

    companion object {
        private const val LOG_TAG: String = "MediaPlaybackService"
        const val MEDIA_ROOT_ID = "root"
        const val LIVE_ROOT_ID = "LIVE_RADIO"
        const val NOTIFICATION_CHANNEL_ID = "audio"
        const val NOTIFICATION_ID = 874
        private val liveHitradioUrl = SourceUrl(
            "http://stream2.hit.hu:8080/speech",
            "http://stream2.hit.hu:8080/low",
            "http://stream2.hit.hu:8080/high",
        )
    }

}