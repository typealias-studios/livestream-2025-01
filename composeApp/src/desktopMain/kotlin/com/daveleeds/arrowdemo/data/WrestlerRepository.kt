package com.daveleeds.arrowdemo.data

import arrow.resilience.Schedule
import arrow.resilience.retry
import com.daveleeds.arrowdemo.API_URL
import com.daveleeds.arrowdemo.Wrestler
import com.daveleeds.arrowdemo.WrestlerIds
import com.daveleeds.arrowdemo.defaultClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class WrestlerRepository(
    private val baseUrl: String = API_URL,
    private val client: HttpClient = defaultClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val policy = Schedule
        .exponential<Throwable>(250.milliseconds)
        .doUntil { _, duration -> duration > 30.seconds }

    suspend fun fetchWrestlerIds(): List<Int> = withContext(dispatcher) {
        policy.retry { client.get(baseUrl).body<WrestlerIds>().ids }
    }

    suspend fun fetchWrestler(id: Int): Wrestler = withContext(dispatcher) {
        policy.retry { client.get("$baseUrl/$id").body() }
    }

    suspend fun saveWrestler(wrestler: Wrestler): Wrestler = withContext(dispatcher) {
        policy.retry {
            client.put("$baseUrl/${wrestler.id}") {
                contentType(ContentType.Application.Json)
                setBody(wrestler)
            }.body()
        }
    }
}
