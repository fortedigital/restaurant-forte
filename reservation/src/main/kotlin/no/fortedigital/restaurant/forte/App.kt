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
import no.fortedigital.restaurant.forte.kafka.KafkaProducer
import no.fortedigital.restaurant.forte.reservation.reservation

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
    val reservationProducer = KafkaProducer()

    configureRouting(ioCoroutineScope, reservationProducer)
    configureSerialization()
}

private fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json = Json {
            ignoreUnknownKeys = true
        })
    }
}

private fun Application.configureRouting(ioScope: CoroutineScope, reservationProducer: KafkaProducer) {
    routing {
        reservation(ioScope, reservationProducer)
        health()
    }
}

private fun Routing.health() {
    route("health") {
        get("alive") {
            call.respond("ALIVE")
        }
        get("ready") {
            call.respond("READY")
        }
    }
}

