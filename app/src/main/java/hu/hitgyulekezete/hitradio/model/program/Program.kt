package hu.hitgyulekezete.hitradio.model.program

data class Program(
    val id: String,
    val name: String,
    val picture: String?,
    val description: String?
) {
    val pictureOrDefault: String
        get() = picture ?: "https://myonlineradio.hu/public/uploads/radio_img/hit-radio/play_250_250.jpg"
}