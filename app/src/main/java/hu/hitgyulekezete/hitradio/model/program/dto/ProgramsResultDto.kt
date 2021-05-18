package hu.hitgyulekezete.hitradio.model.program.dto

data class ProgramsResultDto(
    val schedule: List<Day>
) {

    data class Day(
        val day: String,
        val events: List<Event>
    )

    data class Event(
        val show_id: String,
        val show_title: String,
        val show_time: String,
        val show_time_end: String,
        val show_description: String,
        val show_replay: String,
    ) {

    }
}



