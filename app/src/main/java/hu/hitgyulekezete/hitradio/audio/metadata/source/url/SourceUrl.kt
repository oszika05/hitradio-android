package hu.hitgyulekezete.hitradio.audio.metadata.source.url

class SourceUrl {
    private val sources = HashMap<StreamQuality, String>()

    constructor(url: String) {
        sources[StreamQuality.Low] = url
        sources[StreamQuality.Medium] = url
        sources[StreamQuality.High] = url
    }

    constructor(low: String, medium: String, high: String) {
        sources[StreamQuality.Low] = low
        sources[StreamQuality.Medium] = medium
        sources[StreamQuality.High] = high
    }

    operator fun get(index: StreamQuality): String {
        return sources[index]!!
    }
}