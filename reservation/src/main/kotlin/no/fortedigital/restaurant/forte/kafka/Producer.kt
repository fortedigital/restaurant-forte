package no.fortedigital.restaurant.forte.kafka

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.fortedigital.models.event.EventBusProducer
import no.fortedigital.models.event.EventMessage
import no.fortedigital.models.event.Identifiable
import no.fortedigital.restaurant.forte.jsonFormatter
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

internal class Producer : EventBusProducer<Identifiable>, AutoCloseable {
    private companion object {
        private val properties = mapOf(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to "localhost:29092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.CLIENT_ID_CONFIG to "reservation", //FIXME make use of env,
        )
    }

    private val producer = KafkaProducer<String, String>(properties)

    override suspend fun produce(topic: String, message: String, key: String?) {
        producer.send(ProducerRecord(topic, key, message))
    }

    override fun close() = producer.close()
}