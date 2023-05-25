package no.fortedigital.restaurant.forte

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import no.fortedigital.models.event.*
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

internal class ConfirmationBus : EventBus, AutoCloseable {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val river = River(interestingTypes = setOf("RESERVATION_CREATED"))

    private companion object {
        private val consumerProperties = mapOf(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to "localhost:29092",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.GROUP_ID_CONFIG to "confirmation", //FIXME make use of env,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false
        )
        private val producerProperties = mapOf(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to "localhost:29092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.CLIENT_ID_CONFIG to "confirmation", //FIXME make use of env,
        ).toProperties()
    }

    private val producer = KafkaProducer<String, String>(producerProperties)
    private val consumer = KafkaConsumer<String, String>(consumerProperties)

    override fun close() = consumer.close()

    override suspend fun produce(topic: String, message: String, key: String?) {
        producer.send(ProducerRecord(topic, key, message))
    }

    override suspend fun consume(topic: String, handler: suspend (List<Packet>) -> Unit) = coroutineScope {
        consumer.subscribe(listOf(topic))
        while (isActive) {
            val records = consumer.poll(1.seconds.toJavaDuration())
            val messages = records.mapNotNull { record ->
                river.handle(record.value()) { packet ->
                    packet
                }
            }
            if (messages.isNotEmpty()) {
                handler(messages)
                consumer.commitAsync()
            }
        }
        consumer.unsubscribe()
    }
}