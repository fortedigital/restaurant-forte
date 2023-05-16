package no.fortedigital.restaurant.forte.reservation

import no.fortedigital.models.event.EventMessage
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

private const val reservationEventType = "RESERVATION_CREATED"

class Reservation(
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime,
    val totalGuests: TotalGuests,
    val id: UUID = UUID.randomUUID()
) {
    init {
        require(startTime <= endTime) {
            "Reservation dates are illegal"
        }
    }

    val duration
        get() = (endTime.toInstant().epochSecond
                - startTime.toInstant().epochSecond).seconds
}

internal fun Reservation.toEventMessage() = EventMessage(
    type = reservationEventType,
    payload = ReservationDTO(
        startTime = startTime,
        endTime = endTime,
        totalGuests = totalGuests.amount,
        initiator = "guest@example.com",
        id = id
    )
)