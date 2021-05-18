package hu.hitgyulekezete.hitradio.audio.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.scheduler.Scheduler
import hu.hitgyulekezete.hitradio.R
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import com.google.android.exoplayer2.util.NotificationUtil

import android.content.Context
import java.lang.Exception


class DownloadService : com.google.android.exoplayer2.offline.DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    hu.hitgyulekezete.hitradio.R.string.download_notification_channel_name,
    hu.hitgyulekezete.hitradio.R.string.download_notification_channel_description
) {

    override fun getDownloadManager(): DownloadManager {
        return getDownloadManager(this)
    }

    override fun getScheduler(): Scheduler? {
        return if (Util.SDK_INT >= 21) {
            PlatformScheduler(this, JOB_ID)
        } else {
            null
        }
    }

    override fun getForegroundNotification(downloads: MutableList<Download>): Notification {
        return DownloadNotificationHelper(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .buildProgressNotification(
                this,
                hu.hitgyulekezete.hitradio.R.drawable.ic_download,
                null,
                null,
                downloads
            );
    }

    private class TerminalStateNotificationHelper(
        context: Context,
        private val notificationHelper: DownloadNotificationHelper, firstNotificationId: Int
    ) :
        DownloadManager.Listener {
        private val context: Context = context.applicationContext
        private var nextNotificationId: Int = firstNotificationId
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            val notification: Notification = when (download.state) {
                Download.STATE_COMPLETED -> {
                    notificationHelper.buildDownloadCompletedNotification(
                        context,
                        hu.hitgyulekezete.hitradio.R.drawable.ic_download_done,
                        null,
                        Util.fromUtf8Bytes(download.request.data)
                    )
                }
                Download.STATE_FAILED -> {
                    notificationHelper.buildDownloadFailedNotification(
                        context,
                        hu.hitgyulekezete.hitradio.R.drawable.ic_download_done,
                        null,
                        Util.fromUtf8Bytes(download.request.data)
                    )
                }
                else -> {
                    return
                }
            }
            NotificationUtil.setNotification(context, nextNotificationId++, notification)
        }

    }

    companion object {
        private const val FOREGROUND_NOTIFICATION_ID = 213
        private const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "offline_downloads"
        private const val JOB_ID = 12
        private const val DOWNLOAD_CONTENT_DIRECTORY = "hitradio/"


        private var downloadManager: DownloadManager? = null
        fun getDownloadManager(context: Context): DownloadManager {
            if (downloadManager != null) {
                return downloadManager!!
            }

            CookieHandler.setDefault(
                CookieManager().apply {
                    setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)

                }
            )

            val downloadDir = File(context.noBackupFilesDir, DOWNLOAD_CONTENT_DIRECTORY)

            val downloadManager = DownloadManager(
                context,
                ExoDatabaseProvider(context),
                SimpleCache(
                    downloadDir,
                    NoOpCacheEvictor(),
                    ExoDatabaseProvider(context)
                ),
                DefaultHttpDataSource.Factory().apply {
                    setUserAgent("ANDROID_DOWNLOADER")
                },
                Executors.newFixedThreadPool(6)
            )

            val downloadNotificationHelper =
                DownloadNotificationHelper(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID)

            downloadManager.addListener(
                TerminalStateNotificationHelper(
                    context, downloadNotificationHelper, FOREGROUND_NOTIFICATION_ID + 1
                )
            )

            this.downloadManager = downloadManager
            return downloadManager
        }
    }
}

