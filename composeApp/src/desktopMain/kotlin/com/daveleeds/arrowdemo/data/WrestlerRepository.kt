package com.daveleeds.arrowdemo.data

import com.daveleeds.arrowdemo.API_URL
import com.daveleeds.arrowdemo.Wrestler
import com.daveleeds.arrowdemo.WrestlerIds
import com.daveleeds.arrowdemo.defaultClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WrestlerRepository(
    private val baseUrl: String = API_URL,
    private val client: HttpClient = defaultClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun fetchWrestlerIds(): List<Int> = withContext(dispatcher) {
        client.get(baseUrl).body<WrestlerIds>().ids
    }

    suspend fun fetchWrestler(id: Int): Wrestler = withContext(dispatcher) {
        client.get("$baseUrl/$id").body()
    }

    suspend fun saveWrestler(wrestler: Wrestler): Wrestler = withContext(dispatcher) {
        client.put("$baseUrl/${wrestler.id}") {
            contentType(ContentType.Application.Json)
            setBody(wrestler)
        }.body()
    }
}
