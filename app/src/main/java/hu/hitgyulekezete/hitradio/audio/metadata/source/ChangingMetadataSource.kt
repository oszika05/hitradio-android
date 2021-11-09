package hu.hitgyulekezete.hitradio.audio.metadata.source

import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.audio.service.MediaPlaybackService
import hu.hitgyulekezete.hitradio.audio.service.SourceMediaItem
import hu.hitgyulekezete.hitradio.model.programguide.current.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem

class ChangingMetadataSource(
    override val id: String,
    override val name: String,
    override val description: String?,
    override val url: SourceUrl,
    private val currentProgramRepository: CurrentProgramRepository,
) : Source, CurrentProgramRepository.OnChangeListener {

    private var observer: Source.MetadataObserver? = null

    private var _metadata: Metadata = Metadata.Empty

    override val metadata: Metadata
        get() = _metadata

    override fun observe(observer: Source.MetadataObserver) {
        currentProgramRepository.start()
        currentProgramRepository.addObserver(this)
        this.observer = observer
        observer.onMetadataChanged(_metadata)
    }

    override fun removeObserver(observer: Source.MetadataObserver) {
        if (this.observer == observer) {
            currentProgramRepository.end()
            this.observer = null
        }

    }

    fun end() {
        currentProgramRepository?.end()
    }

    override fun onCurrentProgramChange(newProgram: ProgramGuideItem?) {
        _metadata = Metadata(
            title = newProgram?.titleWithReplay ?: "",
            subtitle = name,
            artUri = null, // TODO
            type = MetadataType.LIVE
        )

        observer?.onMetadataChanged(_metadata)
    }

    companion object {

    }
}

fun ChangingMetadataSource.Companion.live(currentProgramRepository: CurrentProgramRepository): ChangingMetadataSource {
    return ChangingMetadataSource(
        id = LIVE_ID,
        name = "Élő adás",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt rutrum mi ut congue. Fusce pellentesque venenatis placerat. Phasellus hendrerit nisi est, sit amet feugiat.",
        url = SourceUrl(
            low = "http://stream2.hit.hu:8080/speech",
            medium = "http://stream2.hit.hu:8080/low",
            high = "http://stream2.hit.hu:8080/high",
        ),
        currentProgramRepository = currentProgramRepository
    )
}

val ChangingMetadataSource.Companion.LIVE_ID: String
    get() = "LIVE_HITRADIO"