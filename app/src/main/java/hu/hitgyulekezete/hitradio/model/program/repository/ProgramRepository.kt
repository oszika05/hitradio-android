package hu.hitgyulekezete.hitradio.model.program.repository

import hu.hitgyulekezete.hitradio.model.news.News
import hu.hitgyulekezete.hitradio.model.news.NewsRepository
import hu.hitgyulekezete.hitradio.model.program.Episode
import hu.hitgyulekezete.hitradio.model.program.Person
import hu.hitgyulekezete.hitradio.model.program.PersonType
import hu.hitgyulekezete.hitradio.model.program.Program
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProgramRepository {

    private val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getPrograms(from: Int, pageSize: Int, search: String? = null): List<Program> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Program>>("${ProgramRepository.endpoint}/program") {
                    parameter("from", from)
                    parameter("pageSize", pageSize)

                    if (search != null && search.isNotBlank()) {
                        parameter("search", search)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    suspend fun getEpisodes(
        from: Int,
        pageSize: Int,
        programId: String? = null,
        search: String? = null,
        tags: List<String> = listOf(),
        people: List<String> = listOf()
    ): List<Episode> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Episode>>("${ProgramRepository.endpoint}/episode") {
                    parameter("from", from)
                    parameter("pageSize", pageSize)

                    if (programId != null) {
                        parameter("programId", programId)
                    }

                    if (search != null && search.isNotBlank()) {
                        parameter("search", search)
                    }

                    if (tags.isNotEmpty()) {
                        parameter("tag", tags)
                    }

                    if (people.isNotEmpty()) {
                        parameter("person", people)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    suspend fun getPeople(from: Int, pageSize: Int, search: String?, type: PersonType?): List<Person> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Person>>("${ProgramRepository.endpoint}/person") {
                    parameter("from", from)
                    parameter("pageSize", pageSize)

                    if (search != null && search.isNotBlank()) {
                        parameter("search", search)
                    }

                    if (type != null) {
                        parameter("type", type)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }


    suspend fun getRelatedEpisodes(episodeId: String): List<Episode> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Episode>>("${ProgramRepository.endpoint}/episode/$episodeId/related")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    suspend fun getRelatedPeople(personId: String): List<Person> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Person>>("${ProgramRepository.endpoint}/person/$personId/related")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    companion object {
        private val endpoint = "https://hitradio-mock.herokuapp.com"
    }
}