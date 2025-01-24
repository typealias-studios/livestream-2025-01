package com.daveleeds.arrowdemo

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
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

    suspend fun saveWrestler(wrestler: Wrestler) = withContext(dispatcher) {
        client.put("$baseUrl/${wrestler.id}") {
            contentType(ContentType.Application.Json)
            setBody(wrestler)
        }
    }
}
