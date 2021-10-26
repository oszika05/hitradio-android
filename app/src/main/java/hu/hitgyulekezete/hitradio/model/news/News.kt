package hu.hitgyulekezete.hitradio.model.news

import java.util.*

data class News(
    val id: String,
    val title: String,
    val picture: String,
    val date: Date,
    val tags: List<String>,
    val content: String
)
