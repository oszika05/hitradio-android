package hu.hitgyulekezete.hitradio.model.program.api

import android.util.Log
import hu.hitgyulekezete.hitradio.model.program.Program
import hu.hitgyulekezete.hitradio.model.program.api.dto.ProgramsResultDto
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class ProgramApi(
    private val endpoint: String
) {
    private val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    private fun parseDate(day: Int, str: String, isStart: Boolean = true): Date? {
        var hour = 0
        var minute = 0
        try {
            val parts = str.split(":")
            hour = parts[0].toInt()
            minute = parts[1].toInt()
        } catch (e: Exception) {
            return null
        }

        return Calendar.getInstance().run {
            set(Calendar.DAY_OF_WEEK, day + 1)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, if (isStart) 0 else 59)
            set(Calendar.MILLISECOND, 0)

            if (!isStart) {
                add(Calendar.MINUTE, -1)
            }

            time
        }
    }

    private fun convertPrograms(programs: ProgramsResultDto): List<Program> {

        return programs.schedule.foldIndexed(listOf<Program>()) { day, programs, events ->
            Log.d("fold", "fold $day")
            programs + events.events
                .filter { event ->
                    event.show_time.contains(':') && event.show_time_end.contains(':')
                }
                .map { event ->
                    Triple(
                        event,
                        this.parseDate(day, event.show_time, isStart = true),
                        this.parseDate(day, event.show_time_end, isStart = false)
                    )
                }
                .filter { (_, start, end) ->
                    start != null && end != null
                }
                .map { (event, start, end) ->
                    Program(
                        id = event.show_id,
                        title = event.show_title,
                        start = start!!,
                        end = end!!,
                        description = event.show_description,
                        replay = event.show_replay,
                    )
                }
        }
    }

    suspend fun get(): List<Program> {
        return try {
            withContext(Dispatchers.IO) {
                val result = client.get<ProgramsResultDto>(endpoint)
                convertPrograms(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }
}