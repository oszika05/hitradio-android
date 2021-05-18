package hu.hitgyulekezete.hitradio

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.rememberNavController
import hu.hitgyulekezete.hitradio.audio.AudioController
import hu.hitgyulekezete.hitradio.audio.VolumeObserver
import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.ChangingMetadataSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.SimpleSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService
import hu.hitgyulekezete.hitradio.model.program.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.api.ProgramApi
import hu.hitgyulekezete.hitradio.view.Pages
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    val audioController: AudioController by lazy {
        AudioController(this@MainActivity)
    }

    private val programApi = ProgramApi(endpoint = "https://www.hitradio.hu/api/musor_ios.php")
    private var currentProgramRepository: CurrentProgramRepository = CurrentProgramRepository(listOf())

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

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
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

        setContent {

            val navController = rememberNavController()

            val metadata = audioController.metadata.observeAsState()
            val playbackState = audioController.playbackState.observeAsState()
            val scope = rememberCoroutineScope()

            val volume = volumeObserver.volume.observeAsState(0.0f)


            NowPlayingBarLayout(
                metadata = metadata.value ?: Metadata.Empty,
                playbackState = playbackState.value ?: AudioController.PlaybackState.STOPPED,
                seekPercentage = 0.2f,
                volumePercentage = volume.value,
                onSeekTo = { /*TODO*/ },
                onSetVolume = {
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        (it * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).roundToInt(),
                        0
                    )
                },
                onPlayPausePressed = {
                    audioController.playPause()
                }) {

                Pages(
                    navController = navController,
                    audioController = audioController,
                    programApi = programApi,
                    programRepository = currentProgramRepository
                )
            }
        }
    }
}