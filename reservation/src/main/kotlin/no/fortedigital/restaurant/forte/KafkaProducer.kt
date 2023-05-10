package no.fortedigital.restaurant.forte

import kotlinx.datetime.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.fortedigital.models.event.EventBusProducer
import no.fortedigital.models.event.EventMessage
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.security.auth.SecurityProtocol
import org.apache.kafka.common.serialization.StringSerializer
import java.time.ZoneId


fun produce() {
    val producer = KafkaProducer() // TODO only dummy topic for now

    val randomStartTime = (0 until 22).random()
    val randomGuestCount = (1 until 12).random()

    val startTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = randomStartTime, minute = 0)
        .toJavaLocalDateTime().atZone(
            ZoneId.systemDefault()
        )
    val endTime = Clock.System.todayIn(TimeZone.currentSystemDefault()).atTime(hour = randomStartTime + 1, minute = 0)
        .toJavaLocalDateTime().atZone(
            ZoneId.systemDefault()
        )
    val totalGuests = TotalGuests(amount = randomGuestCount)

    val reservation = Reservation(startTime = startTime, endTime = endTime, totalGuests = totalGuests).toDTO()

    println("Producing message!")
    producer.produce(topic = "restaurant-forte-rapid-v1", message = reservation)
    println("Closing record...")
    println("Producer closed")
}

class KafkaProducer : EventBusProducer<ReservationDTO> {
    private companion object {
        private val properties = mapOf(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to "localhost:29092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.CLIENT_ID_CONFIG to "reservation", //FIXME make use of env,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to SecurityProtocol.PLAINTEXT.name
        )
    }

    private val producer = KafkaProducer<String, String>(properties)

    override fun produce(topic: String, message: EventMessage<ReservationDTO>) {
        val json = Json.encodeToString(message)
        return producer.use {
            it.send(ProducerRecord(topic, json))
        }
    }

}