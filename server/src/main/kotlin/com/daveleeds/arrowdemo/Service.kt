package com.daveleeds.arrowdemo

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) { json(Json { prettyPrint = true }) }
    routing {
        get("/wrestlers") {
            call.respond(WrestlerIds(wrestlers.map { it.id }))
        }

        get("wrestlers/{id}") {
            delay(1.seconds)
            call.respond(wrestlers.first { it.id == call.parameters["id"]?.toInt() })
        }

        staticResources("/images", "images")
    }
}

private val wrestlers = listOf(
    Wrestler(123, "Sledge", 32, 220, Hometown("Nashville", "USA")),
    Wrestler(456, "Hammer", 33, 190, Hometown("Copenhagen", "Denmark")),
    Wrestler(789, "Vandal", 30, 195, Hometown("Phoenix", "USA"))
)
