package hu.hitgyulekezete.hitradio.model.common.date

import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

data class Days(
    val sinceNow: Int,
    val date: Date,
) {

}

fun Date.daysSince(): Days {
    val now = Date()

    val timeDifference = now.time - time

    return Days(
        sinceNow = TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS).toInt(),
        date = this,
    )
}

private val dateFormat = SimpleDateFormat("yyyy.MM.dd.", Locale.getDefault())

fun Days.toReadableString(): String {
    return when (this.sinceNow) {
        0 -> "Ma"
        1 -> "Tegnap"
        else -> {
            dateFormat.format(this.date)
        }
    }
}