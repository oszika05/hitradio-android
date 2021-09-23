package hu.hitgyulekezete.hitradio.audio.controller

import android.media.session.PlaybackState
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata

class AudioStateManager(
    private val mediaController: MediaControllerCompat
) {

    private val _playbackState = MutableLiveData<PlaybackState>(PlaybackState.STOPPED)
    val playbackState: LiveData<PlaybackState> = _playbackState

    private val controllerCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _playbackState.postValue(PlaybackState.fromPlaybackStateCompat(state?.state))
        }
    }

    fun connect() {
        mediaController.registerCallback(controllerCallback)
    }

    fun disconnect() {
        mediaController.unregisterCallback(controllerCallback)
    }


    fun playPause() {
        if (playbackState.value == PlaybackState.PLAYING || playbackState.value == PlaybackState.BUFFERING) {
            mediaController.transportControls.pause()
        } else {
            mediaController.transportControls.play()
        }
    }


    enum class PlaybackState {
        PLAYING,
        PAUSED,
        BUFFERING,
        STOPPED;

        fun toggle(): PlaybackState {
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