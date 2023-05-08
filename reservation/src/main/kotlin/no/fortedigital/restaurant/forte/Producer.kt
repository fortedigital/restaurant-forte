/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package no.fortedigital.restaurant.forte

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.security.auth.SecurityProtocol
import org.apache.kafka.common.serialization.StringSerializer

fun main() {
    val properties = mapOf(
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.CLIENT_ID_CONFIG to "reservation", //FIXME make use of env,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to SecurityProtocol.PLAINTEXT.name

    )
    val producer = KafkaProducer<String, String>(properties)

    val topic = "my-topic" // TODO only dummy topic for now
    val message = """
        {
            "message": "Hello from reservation microservice"
        }
    """.trimIndent()
    val record = ProducerRecord<String, String>(topic, message)

    println("Record: ${record.timestamp()} - ${record.value()} ")
    producer.send(record) { rec, _ ->
        println("Record sent! ${rec.timestamp()}")

    }
    println("Closing record...")
    producer.close()
    println("Producer closed")
}