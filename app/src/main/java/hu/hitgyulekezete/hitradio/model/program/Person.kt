package hu.hitgyulekezete.hitradio.model.program

import com.google.gson.annotations.SerializedName

data class Person(
    val id: String,
    val name: String,
    val type: PersonType,
    val picture: String?,
    val description: String?
)

enum class PersonType {
    @SerializedName("guest")
    Guest,
    @SerializedName("host")
    Host
}