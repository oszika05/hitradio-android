package hu.hitgyulekezete.hitradio

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.hitgyulekezete.hitradio.audio.VolumeObserver
import hu.hitgyulekezete.hitradio.audio.controller.AudioController
import hu.hitgyulekezete.hitradio.audio.controller.DownloadManager
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.programguide.api.ProgramGuideApi
import hu.hitgyulekezete.hitradio.view.pages.Layout
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val audioController: AudioController by lazy {
        AudioController(this@MainActivity)
    }

    private val programApi = ProgramGuideApi(endpoint = "https://www.hitradio.hu/api/musor_ios.php")
    private var currentProgramRepository: CurrentProgramRepository =
        CurrentProgramRepository(listOf())

    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY)


    override fun onResume() {
        super.onResume()

        audioController.connect()
        currentProgramRepository.start()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        super.onStop()

        audioController.disconnect()
        currentProgramRepository.end()
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val programs = programApi.get()
            currentProgramRepository.setPrograms(programs)
            currentProgramRepository.start()
        }

        volumeControlStream = AudioManager.STREAM_MUSIC
        val audioManager =
            applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val volumeObserver = VolumeObserver(Handler(), audioManager)

        this.applicationContext.contentResolver.registerContentObserver(
            android.provider.Settings.System.CONTENT_URI,
            true,
            volumeObserver
        )

        val downloadManager = DownloadManager(this)

        setContent {

            val navController = rememberNavController()

            val metadata = audioController.metadata.collectAsState()
            val playbackState = audioController.playbackState.collectAsState()
            val scope = rememberCoroutineScope()

            val volume = volumeObserver.volume.observeAsState(0.0f)


            Layout(
                audioController = audioController,
                audioManager = audioManager,
                volumeObserver = volumeObserver,
            )
        }
    }
}