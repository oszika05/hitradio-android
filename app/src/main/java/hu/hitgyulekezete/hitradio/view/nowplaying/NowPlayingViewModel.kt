package hu.hitgyulekezete.hitradio.view.nowplaying

import android.app.Activity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import hu.hitgyulekezete.hitradio.audio.AudioController
import hu.hitgyulekezete.hitradio.audio.metadata.source.ChangingMetadataSource
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService
import hu.hitgyulekezete.hitradio.model.program.api.ProgramApi

class NowPlayingViewModel(activity: Activity) : ViewModel() {
    val audioController = AudioController(activity = activity).apply {
        setSource(ChangingMetadataSource(
            id = MediaPlaybackService.LIVE_HITRADIO_ID,
            name = "Hitrádió",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt rutrum mi ut congue. Fusce pellentesque venenatis placerat. Phasellus hendrerit nisi est, sit amet feugiat.",
            url = SourceUrl(url = "http://stream2.hit.hu:8080/high"),
            programApi = ProgramApi("https://www.hitradio.hu/api/musor_ios.php")
        ))
    }
}