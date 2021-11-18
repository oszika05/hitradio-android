package hu.hitgyulekezete.hitradio.audio.controller

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MetadataManager(
    private val controller: MediaControllerCompat
) {
    private val _metadata = MutableStateFlow<Metadata>(Metadata.from(null))
    val metadata: StateFlow<Metadata> = _metadata

    private val controllerCallback = object : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            Log.d("ALMA", "onMetadataChanged: ${metadata?.description}")
            _metadata.value = Metadata.from(metadata?.description)
        }
    }

    fun connect() {
        controller.registerCallback(controllerCallback)
    }

    fun disconnect() {
        controller.unregisterCallback(controllerCallback)
    }
}