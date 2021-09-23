package hu.hitgyulekezete.hitradio.audio.controller

import android.app.Activity
import android.net.Uri
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.Source
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.StreamQuality

class DownloadManager(
    private val activity: Activity,
) {
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
}