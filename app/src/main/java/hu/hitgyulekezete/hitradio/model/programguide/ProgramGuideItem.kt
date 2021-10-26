package hu.hitgyulekezete.hitradio.model.programguide

import java.util.*

data class ProgramGuideItem(
    val id: String,
    val title: String,
    val start: Date,
    val end: Date,
    val description: String,
    val replay: String // TODO??
) {
    fun isCurrentlyPlaying(date: Date): Boolean {
        return date.after(start) && date.before(end)
    }

    val titleWithReplay: String
        get() = "$title $replay".trim()
}