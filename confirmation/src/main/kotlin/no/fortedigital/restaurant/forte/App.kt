package no.fortedigital.restaurant.forte

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.fortedigital.models.event.EventMessage
import java.util.*

fun main() = runBlocking {
    val eventBus = ConfirmationBus()
    eventBus.consume("restaurant-forte-rapid-v1") { messages ->
        messages.forEach { packet ->
            val dto = ConfirmationDTO(id = packet.inner("payload").uuid("id") ?: UUID.randomUUID())
            val eventMessage = EventMessage(type = "RESERVATION_CONFIRMED", payload = dto, id = dto.id)
            eventBus.produce(
                "restaurant-forte-rapid-v1", Json.encodeToString(eventMessage), eventMessage.id.toString()
            )
        }
    }
}