package hu.hitgyulekezete.hitradio.audio.controller.seekposition

import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import okhttp3.internal.toLongOrDefault

fun MediaMetadataCompat.getLengthInMs(): Long? {
    val duration = getLong(MediaMetadataCompat.METADATA_KEY_DURATION) ?: -1

    if (duration <= 0 || duration >= Long.MAX_VALUE) {
        return null
    }

    return duration
}

