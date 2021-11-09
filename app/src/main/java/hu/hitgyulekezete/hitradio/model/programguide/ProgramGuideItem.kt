package hu.hitgyulekezete.hitradio.model.programguide

import java.text.SimpleDateFormat
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

    companion object {}
}

//private val hourFormat = SimpleDateFormat("HH:mm")
private val hourFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

val ProgramGuideItem.startHourString: String
    get() {
        return hourFormat.format(start)
    }

val ProgramGuideItem.day: String
    get() {
        val day = Calendar.getInstance().run {
            time = start
            get(Calendar.DAY_OF_WEEK)
        }

        return ProgramGuideItem.getStringForDay(day)
    }

fun ProgramGuideItem.Companion.getStringForDay(day: Int): String {
    return when (day) {
        Calendar.MONDAY -> "Hétfő"
        Calendar.TUESDAY -> "Kedd"
        Calendar.WEDNESDAY -> "Szerda"
        Calendar.THURSDAY -> "Csütrörtök"
        Calendar.FRIDAY -> "Péntek"
        Calendar.SATURDAY -> "Szombat"
        Calendar.SUNDAY -> "Vasárnap"
        else -> ""
    }
}