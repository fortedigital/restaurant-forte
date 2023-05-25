package no.fortedigital.restaurant.forte

import kotlinx.serialization.Serializable
import no.fortedigital.models.event.Identifiable
import no.fortedigital.models.event.UUIDSerializer
import java.util.*

@Serializable
data class ConfirmationDTO(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID = UUID.randomUUID(),
) : Identifiable
