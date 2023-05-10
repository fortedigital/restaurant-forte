package no.fortedigital.restaurant.forte.kafka

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.fortedigital.models.event.EventBusProducer
import no.fortedigital.models.event.EventMessage
import no.fortedigital.restaurant.forte.reservation.ReservationDTO
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.security.auth.SecurityProtocol
import org.apache.kafka.common.serialization.StringSerializer

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

    override suspend fun produce(topic: String, message: EventMessage<ReservationDTO>) {
        val json = Json.encodeToString(message)
        producer.send(ProducerRecord(topic, message.payload.id.toString(), json))
    }
}