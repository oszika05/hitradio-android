package hu.hitgyulekezete.hitradio.model.news

import android.util.Log
import hu.hitgyulekezete.hitradio.model.program.api.dto.ProgramsResultDto
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception

class NewsRepository {

    private val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getNews(
        from: Int = 0,
        pageSize: Int = 20,
        search: String? = null,
        tags: List<String> = listOf()
    ): List<News> {
        Log.d("ALMA", "getNews(from = $from, pageSize: $pageSize, search: $search)")
        return try {
            withContext(Dispatchers.IO) {
                client.get<List<News>>(endpoint) {
                    parameter("from", from)
                    parameter("pageSize", pageSize)
                    if (search != null && search.isNotBlank()) {
                        parameter("search", search)
                    }
                    if (tags.isNotEmpty()) {
                        parameter("tag", tags)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    suspend fun getNewsItem(id: String): News? {
        return try {
            withContext(Dispatchers.IO) {
                client.get<News>("$endpoint/$id")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private val endpoint = "https://hitradio-mock.herokuapp.com/news"
    }

}