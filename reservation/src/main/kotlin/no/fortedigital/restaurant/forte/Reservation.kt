package no.fortedigital.restaurant.forte

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.seconds

class Reservation(
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime,
    val totalGuests: TotalGuests
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

@Serializable
data class ReservationDTO(
    val startTime: String,
    val endTime: String,
    val totalGuests: Int,
    val initiator: String
)

internal fun Reservation.toDTO(): ReservationDTO {
    return ReservationDTO(
        startTime = startTime.toString(),
        endTime = endTime.toString(),
        totalGuests = totalGuests.amount,
        initiator = "guest@example.com"
    )
}