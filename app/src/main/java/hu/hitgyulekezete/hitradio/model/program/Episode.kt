package hu.hitgyulekezete.hitradio.model.program

import java.util.*

data class Episode(
    val id: String,
    val title: String,
    val date: Date,
    val description: String?,
    val tags: List<String>,
    val program: Program,
    val audioUrl: String,
    val hosts: List<Person>,
    val guests: List<Person>,
)
