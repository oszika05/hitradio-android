package hu.hitgyulekezete.hitradio.audio.controller.seekposition

import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService
import okhttp3.internal.toLongOrDefault
import kotlin.math.roundToLong


class SeekPositionManager(
    private val mediaController: MediaControllerCompat,
    private val handler: Handler
) {
    private val _seekPosition = MutableLiveData<Float?>(null)
    val seekPosition: LiveData<Float?> = _seekPosition

    private val positionUpdater = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1000)

            val length = mediaController.metadata.getLengthInMs()

            if (length == null) {
                _seekPosition.postValue(null)
                return
            }

            var seekPercentage: Float? =
                mediaController.playbackState.position.toFloat() / length.toFloat()

            if (seekPercentage ?: -1f < 0) {
                seekPercentage = null
            }

            _seekPosition.postValue(seekPercentage)
        }
    }

    fun start() {
        handler.postDelayed(positionUpdater, 1000)
    }

    fun stop() {
        handler.removeCallbacks(positionUpdater)
    }

    fun seekTo(percentage: Float) {
        val length = mediaController.metadata.getLengthInMs() ?: return

        mediaController.transportControls.seekTo(
            (percentage * length.toFloat()).roundToLong()
        )
    }
}