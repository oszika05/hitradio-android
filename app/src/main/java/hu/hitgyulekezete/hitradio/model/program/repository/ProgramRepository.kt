package hu.hitgyulekezete.hitradio.model.program.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProgramRepository {

    private val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    private suspend fun getIdToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null
        return suspendCoroutine<String?> { cont ->
            user.getIdToken(false)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        cont.resume(it.result?.token)
                    } else {
                        cont.resume(null)
                    }
                }
        }
    }

    suspend fun getProgram(id: String): Program? {
        return try {
            withContext(Dispatchers.IO) {
                client.get<Program?>("${ProgramRepository.endpoint}/program/$id") {
                    FirebaseAuth.getInstance().currentUser?.let { currentUser ->
                        val idToken = currentUser.getIdToken(false)
                        header("token", idToken)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getPrograms(from: Int, pageSize: Int, search: String? = null): List<Program> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Program>>("${ProgramRepository.endpoint}/program") {
                    parameter("from", from)
                    parameter("pageSize", pageSize)

                    getIdToken()?.let { idToken ->
                        header("token", idToken)
                    }

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

                    getIdToken()?.let { idToken ->
                        header("token", idToken)
                    }

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
                        for (person in people) {
                            parameter("person", person)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    suspend fun getPeople(
        from: Int,
        pageSize: Int,
        search: String? = null,
        type: PersonType? = null,
    ): List<Person> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Person>>("${ProgramRepository.endpoint}/person") {
                    parameter("from", from)
                    parameter("pageSize", pageSize)

                    getIdToken()?.let { idToken ->
                        header("token", idToken)
                    }

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

    suspend fun getPerson(personId: String): Person? {
        return try {
            withContext(Dispatchers.IO) {
                client.get<Person?>("${ProgramRepository.endpoint}/person/$personId") {
                    getIdToken()?.let { idToken ->
                        header("token", idToken)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getEpisode(episodeId: String): Episode? {
        return try {
            withContext(Dispatchers.IO) {
                client.get<Episode?>("${ProgramRepository.endpoint}/episode/$episodeId") {
                    getIdToken()?.let { idToken ->
                        header("token", idToken)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun getRelatedEpisodes(episodeId: String): List<Episode> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Episode>>("${ProgramRepository.endpoint}/episode/$episodeId/related") {
                    getIdToken()?.let { idToken ->
                        header("token", idToken)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    suspend fun getRelatedPeople(personId: String): List<Person> {
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<Person>>("${ProgramRepository.endpoint}/person/$personId/related") {
                    getIdToken()?.let { idToken ->
                        header("token", idToken)
                    }
                }
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