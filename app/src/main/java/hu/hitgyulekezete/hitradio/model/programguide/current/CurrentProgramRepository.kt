package hu.hitgyulekezete.hitradio.model.programguide.current

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.hitgyulekezete.hitradio.model.programguide.ProgramGuideItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import kotlin.concurrent.schedule

class CurrentProgramRepository(
    programs: List<ProgramGuideItem>,
    private val defaultProgram: ProgramGuideItem = ProgramGuideItem(
        id = "",
        title = "Zenei válogatás",
        start = Calendar.getInstance().run {
            add(Calendar.MINUTE, 5)
            add(Calendar.HOUR, 1)
            set(Calendar.MINUTE, 0)
            time
        },
        end = Calendar.getInstance().run {
            add(Calendar.MINUTE, -5)
            set(Calendar.MINUTE, 0)
            time
        },
        description = "A legjobb kereszény zenék",
        replay = "",
    )
) {
    private var isStarted = false
    private var programs = programs.sortedBy { it.start }

    private var timer: Timer? = null

    private val observers: MutableList<OnChangeListener> = mutableListOf()

    var currentProgram: ProgramGuideItem? = getCurrent()
        private set

    private val _currentProgramLiveData = MutableLiveData<ProgramGuideItem?>(null)
    val currentProgramLiveData: LiveData<ProgramGuideItem?> = _currentProgramLiveData
    private val _currentOrDefault = MutableStateFlow(defaultProgram)
    val currentOrDefault: StateFlow<ProgramGuideItem> = _currentOrDefault

    private val _nextPrograms = MutableLiveData<List<ProgramGuideItem>>(listOf())
    val nextPrograms: LiveData<List<ProgramGuideItem>> = _nextPrograms

    fun setPrograms(programs: List<ProgramGuideItem>) {
        val wasStarted = isStarted
        if (isStarted) {
            end()
        }

        this.programs = programs

        if (wasStarted) {
            start()
        }
    }

    fun addObserver(observer: OnChangeListener) {
        observers.add(observer)
        observer.onCurrentProgramChange(currentProgram)
    }

    fun removeObserver(observer: OnChangeListener) {
        observers.remove(observer)
    }

    fun getNext(): ProgramGuideItem? {
        val now = Date()

        for (program in programs) {
            if (now.after(program.start)) {
                return program
            }
        }

        return null
    }

    fun getCurrent(): ProgramGuideItem? {
        val now = Date()
        for (program in programs) {
            if (program.isCurrentlyPlaying(now)) {
                return program
            }

            if (program.start.after(now)) {
                break
            }
        }

        return null
    }

    private fun getProgramsAfter(date: Date): List<ProgramGuideItem> {
        return programs.filter { it.start.after(date) }
    }

    private fun updateCurrent() {
        val now = Date()
        val nextPrograms = getProgramsAfter(now)
        _nextPrograms.postValue(nextPrograms)

        val current = getCurrent()

        val nextProgramDateStart = nextPrograms.firstOrNull()?.start

        currentProgram = current
        _currentProgramLiveData.postValue(current)
        _currentOrDefault.value = current ?: defaultProgram.copy(
            start = Calendar.getInstance().run {
                add(Calendar.MINUTE, 3)
                set(Calendar.MINUTE, 0)
                time
            },
            end = Calendar.getInstance().run {
                if (nextProgramDateStart != null) {
                    time = nextProgramDateStart
                } else {
                    add(Calendar.MINUTE, 3)
                    add(Calendar.HOUR, 1)
                    set(Calendar.MINUTE, 0)
                }
                time
            },
        )

        for (observer in observers) {
            observer.onCurrentProgramChange(current)
        }
    }

    private fun scheduleNext() {
        val current = getCurrent()
        val next = getNext()

        val nextTime = current?.end ?: next?.start ?: return

        // Add 5 seconds to deal with small intersection bugs
        val nextPlus5 = Calendar.getInstance().run {
            time = nextTime
            add(Calendar.SECOND, 5)
            time
        }

        timer?.schedule(nextPlus5) {
            updateCurrent()
            scheduleNext()
        }
    }

    fun start() {
        timer?.cancel()
        timer = Timer()

        updateCurrent()
        scheduleNext()

        isStarted = true
    }

    fun end() {
        timer?.cancel()
        timer = null

        isStarted = false
    }

    interface OnChangeListener {
        fun onCurrentProgramChange(newProgram: ProgramGuideItem?)
    }
}