package no.fortedigital.restaurant.forte

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