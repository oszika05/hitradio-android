package hu.hitgyulekezete.hitradio.model.program

import com.google.gson.annotations.SerializedName

data class Person(
    val id: String,
    val name: String,
    val type: PersonType,
    val picture: String?,
    val description: String?
)

val Person?.pictureOrDefault: String
    get() = this?.picture ?: "https://myonlineradio.hu/public/uploads/radio_img/hit-radio/play_250_250.jpg"


enum class PersonType {
    @SerializedName("guest")
    Guest,
    @SerializedName("host")
    Host;

    override fun toString(): String {
        return when(this) {
            Guest -> {
                "guest"
            }
            Host -> {
                "host"
            }
            else -> {
                ""
            }
        }
    }
}