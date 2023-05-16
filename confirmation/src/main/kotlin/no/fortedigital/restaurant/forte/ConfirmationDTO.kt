package no.fortedigital.restaurant.forte

import no.fortedigital.models.event.Identifiable
import java.util.*

data class ConfirmationDTO(override val id: UUID = UUID.randomUUID()) : Identifiable {

}
