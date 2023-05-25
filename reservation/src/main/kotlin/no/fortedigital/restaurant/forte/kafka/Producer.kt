package no.fortedigital.restaurant.forte.kafka

import no.fortedigital.models.event.EventBusProducer
import no.fortedigital.models.event.Identifiable
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

internal class Producer : EventBusProducer, AutoCloseable {
    private val logger = LoggerFactory.getLogger(this::class.java)

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
        producer.send(
            ProducerRecord(
                topic,
                key,
                message
            )
        ) { metadata, exception -> logger.info("PRODUCED MESSAGE WITH TOPIC $topic with content $message") }
    }

    override fun close() = producer.close()
}