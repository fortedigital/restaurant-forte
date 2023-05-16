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
import no.fortedigital.restaurant.forte.kafka.Producer
import no.fortedigital.restaurant.forte.reservation.reservation

private val ioCoroutineScope = CoroutineScope(Dispatchers.IO)

fun main() {
    applicationServer()
        .start(wait = true)
}

fun applicationServer() = embeddedServer(factory = Netty, module = Application::server)

fun Application.server() {
    val reservationProducer = Producer()

    configureRouting(ioCoroutineScope, reservationProducer)
    configureSerialization()
    Runtime.getRuntime().addShutdownHook(Thread {
        ioCoroutineScope.cancel()
        reservationProducer.close()
    })
}

private fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json = jsonFormatter)
    }
}

private fun Application.configureRouting(ioScope: CoroutineScope, reservationProducer: Producer) {
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

