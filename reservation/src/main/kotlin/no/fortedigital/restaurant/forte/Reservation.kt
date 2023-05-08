package no.fortedigital.restaurant.forte

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

class Reservation(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val totalGuests: TotalGuests
) {
    init {
        require(startTime <= endTime) {
            "Reservation dates are illegal"
        }
    }

    val duration
        get() = (endTime.toInstant(TimeZone.currentSystemDefault()).epochSeconds
                        - startTime.toInstant(TimeZone.currentSystemDefault()).epochSeconds).seconds
}

@Serializable
data class ReservationDTO(val startTime: LocalDateTime, val endTime: LocalDateTime, val totalGuests: Int, val initiator: String)

internal fun Reservation.toDTO(): ReservationDTO {
    return ReservationDTO(startTime = startTime, endTime = endTime, totalGuests = totalGuests.amount, initiator = "guest@example.com")
}