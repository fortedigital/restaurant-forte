package no.fortedigital.restaurant.forte.reservation

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.Serializable
import no.fortedigital.restaurant.forte.ZonedDateTimeSerializer
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Serializable
internal data class ReservationPostDTO(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val totalGuests: Int,
    val initiator: String,
)

internal fun ReservationPostDTO.toReservation() =
    Reservation(
        startTime = startTime.toJavaLocalDateTime().atZone(ZoneId.systemDefault()),
        endTime = endTime.toJavaLocalDateTime().atZone(ZoneId.systemDefault()),
        totalGuests = TotalGuests(amount = totalGuests),
        id = UUID.randomUUID()
    )