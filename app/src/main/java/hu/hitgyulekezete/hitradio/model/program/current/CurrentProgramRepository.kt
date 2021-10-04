package hu.hitgyulekezete.hitradio.model.program.current

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.hitgyulekezete.hitradio.audio.service.SourceMediaItem
import hu.hitgyulekezete.hitradio.model.program.Program
import java.util.*
import kotlin.concurrent.schedule

class CurrentProgramRepository(programs: List<Program>) {
    private var isStarted = false
    private var programs = programs.sortedBy { it.start }

    private var timer: Timer? = null

    private val observers: MutableList<OnChangeListener> = mutableListOf()

    var currentProgram: Program? = getCurrent()
        private set

    private val _currentProgramLiveData = MutableLiveData<Program?>(null)
    val currentProgramLiveData: LiveData<Program?> = _currentProgramLiveData

    private val _nextPrograms = MutableLiveData<List<Program>>(listOf())
    val nextPrograms: LiveData<List<Program>> = _nextPrograms

    fun setPrograms(programs: List<Program>) {
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

    fun getNext(): Program? {
        val now = Date()

        for (program in programs) {
            if (now.after(program.start)) {
                return program
            }
        }

        return null
    }

    fun getCurrent(): Program? {
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

    private fun getProgramsAfter(date: Date): List<Program> {
        return programs.filter { it.start.after(date) }
    }

    private fun updateCurrent() {
        val now = Date()
        _nextPrograms.postValue(getProgramsAfter(now))

        val current = getCurrent()

        currentProgram = current
        _currentProgramLiveData.postValue(current)

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
        fun onCurrentProgramChange(newProgram: Program?)
    }
}