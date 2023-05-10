package no.fortedigital.restaurant.forte.reservation

import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

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