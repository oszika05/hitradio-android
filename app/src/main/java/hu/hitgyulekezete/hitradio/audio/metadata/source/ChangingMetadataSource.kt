package hu.hitgyulekezete.hitradio.audio.metadata.source

import hu.hitgyulekezete.hitradio.audio.metadata.Metadata
import hu.hitgyulekezete.hitradio.audio.metadata.MetadataType
import hu.hitgyulekezete.hitradio.audio.metadata.source.url.SourceUrl
import hu.hitgyulekezete.hitradio.model.program.CurrentProgramRepository
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.api.ProgramApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChangingMetadataSource(
    override val id: String,
    override val name: String,
    override val description: String,
    override val url: SourceUrl,
    private val programApi: ProgramApi
) : Source, CurrentProgramRepository.OnChangeListener {

    private var observer: Source.MetadataObserver? = null

    private var currentProgramRepository: CurrentProgramRepository? = null

    private var _metadata: Metadata = Metadata.Empty

    init {
        GlobalScope.launch {
            val programs = programApi.get()

            currentProgramRepository = CurrentProgramRepository(programs = programs)
            currentProgramRepository?.addObserver(this@ChangingMetadataSource)

            currentProgramRepository?.start()
        }
    }

    override val metadata: Metadata
        get() = _metadata

    override fun observe(observer: Source.MetadataObserver) {
        this.observer = observer
        observer.onMetadataChanged(_metadata)
    }

    override fun removeObserver(observer: Source.MetadataObserver) {
        if (this.observer == observer) {
            this.observer = null
        }
    }

    fun end() {
        currentProgramRepository?.end()
    }

    override fun onCurrentProgramChange(newProgram: Program?) {
        _metadata = Metadata(
            title = newProgram?.titleWithReplay ?: "",
            subtitle = name,
            artUri = null, // TODO
            type = MetadataType.LIVE
        )

        observer?.onMetadataChanged(_metadata)
    }
}