package hu.hitgyulekezete.hitradio.audio

import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class VolumeObserver(
    handler: Handler,
    private val audioManager: AudioManager,
): ContentObserver(handler) {
    override fun deliverSelfNotifications(): Boolean {
        return super.deliverSelfNotifications()
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)

        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        _volume.postValue(volume.toFloat() / maxVolume.toFloat())
    }

    private val _volume = MutableLiveData<Float>()
    val volume: LiveData<Float> = _volume
}