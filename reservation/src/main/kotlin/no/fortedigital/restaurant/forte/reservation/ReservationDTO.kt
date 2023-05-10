package no.fortedigital.restaurant.forte.reservation

import kotlinx.serialization.Serializable
import no.fortedigital.models.event.EventMessage
import no.fortedigital.models.event.Identifiable
import no.fortedigital.models.event.UUIDSerializer
import no.fortedigital.restaurant.forte.ZonedDateTimeSerializer
import java.time.ZonedDateTime
import java.util.UUID

private const val reservationEventType = "RESERVATION_CREATED"

@Serializable
data class ReservationDTO(
    @Serializable(with = ZonedDateTimeSerializer::class)
    val startTime: ZonedDateTime,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val endTime: ZonedDateTime,
    val totalGuests: Int,
    val initiator: String,
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID = UUID.randomUUID()
) : Identifiable

internal fun Reservation.toEventMessage() = EventMessage(
    type = reservationEventType,
    payload = ReservationDTO(
        startTime = startTime,
        endTime = endTime,
        totalGuests = totalGuests.amount,
        initiator = "guest@example.com"
    )
)