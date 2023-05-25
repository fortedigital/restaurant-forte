package no.fortedigital.models.event

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.*

class Packet(private val entries: JsonObject) {
    private val interestingEventTypes = mutableSetOf<String>()

    fun int(key: String) = entries[key]?.jsonPrimitive?.int
    fun long(key: String) = entries[key]?.jsonPrimitive?.long
    fun str(key: String) = entries[key]?.jsonPrimitive?.content
    fun date(key: String) = str(key)?.let(ZonedDateTime::parse)
    fun uuid(key: String) = str(key)?.let(UUID::fromString)
    fun inner(key: String): Packet {
        val json = entries[key]?.jsonObject
        if (json.isNullOrEmpty()) {
            return Packet(JsonObject(emptyMap()))
        }
        return Packet(entries = json)
    }

    fun addInterestedIn(vararg interestingType: String) {
        interestingEventTypes += interestingType
    }

    fun isInterestedIn(interestType: String) = interestType in interestingEventTypes

    override fun toString() = Json.encodeToString(entries)
}

private fun String.toPacket(): Packet {
    val jsonElement = Json.parseToJsonElement(this)
    return Packet(entries = jsonElement.jsonObject)
}

class River(private val interestingTypes: Set<String>) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun <T> handle(message: String, onPacket: (Packet) -> T): T? {
        val packet = message.toPacket().apply { addInterestedIn(*interestingTypes.toTypedArray()) }
        if (packet.isInterestedIn(packet.str("@type") ?: "")) {
            logger.info("Received event with type ${packet.str("@type")} for processing")
            return onPacket(packet)
        }
        return null
    }
}