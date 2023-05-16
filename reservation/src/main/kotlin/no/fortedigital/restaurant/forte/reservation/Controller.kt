package no.fortedigital.restaurant.forte.reservation

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import no.fortedigital.restaurant.forte.jsonFormatter
import no.fortedigital.restaurant.forte.kafka.Producer

internal fun Routing.reservation(ioScope: CoroutineScope, producer: Producer) {
    route("reservation") {
        post {
            val reservation = call.receive<ReservationPostDTO>().toReservation()
            ioScope.launch {
                application.log.info("Producing reservation message with id ${reservation.id}")
                val message = reservation.toEventMessage()
                producer.produce(
                    topic = "restaurant-forte-rapid-v1",
                    message = jsonFormatter.encodeToString(message),
                    key = message.payload.id.toString()
                )
            }
            return@post call.respond("Reservation created!")
        }
    }
}