package no.fortedigital.restaurant.forte

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import no.fortedigital.models.event.EventMessage
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override fun deserialize(decoder: Decoder): ZonedDateTime {
        return ZonedDateTime.parse(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }
}

@Serializable
data class ReservationDTO(
    @Serializable(with = ZonedDateTimeSerializer::class)
    val startTime: ZonedDateTime,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val endTime: ZonedDateTime,
    val totalGuests: Int,
    val initiator: String
)

internal fun Reservation.toDTO() = EventMessage(
    type = "RESERVATION_CREATED",
    payload = ReservationDTO(
        startTime = startTime,
        endTime = endTime,
        totalGuests = totalGuests.amount,
        initiator = "guest@example.com"
    )
)

internal fun ReservationDTO.toReservation() =
    Reservation(startTime = startTime, endTime = endTime, totalGuests = TotalGuests(amount = totalGuests))