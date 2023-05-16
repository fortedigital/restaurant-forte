package no.fortedigital.restaurant.forte.reservation

import kotlinx.serialization.Serializable
import no.fortedigital.models.event.Identifiable
import no.fortedigital.models.event.UUIDSerializer
import no.fortedigital.restaurant.forte.ZonedDateTimeSerializer
import java.time.ZonedDateTime
import java.util.*

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