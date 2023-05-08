package no.fortedigital.restaurant.forte

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
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