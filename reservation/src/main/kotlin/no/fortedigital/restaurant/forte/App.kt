package no.fortedigital.restaurant.forte

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

private val ioCoroutineScope = CoroutineScope(Dispatchers.IO)

fun main() {
    applicationServer()
        .apply {
            addShutdownHook {
                println("Shutting down...")
                ioCoroutineScope.cancel()
            }
        }
        .start(wait = true)
}

fun applicationServer() = embeddedServer(factory = Netty, module = Application::server)

fun Application.server() {
    configureRouting(ioCoroutineScope)
    configureSerialization()
}

private fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json = Json {
            ignoreUnknownKeys = true
        })
    }
}

private fun Application.configureRouting(ioScope: CoroutineScope) {
    routing {
        get("/test") {
            ioScope.launch {
                repeat(500_000) {
                    println("Number: $it - thread ${Thread.currentThread()}")
                    delay(500)
                }

            }
            return@get call.respond("My response")
        }
    }
}
