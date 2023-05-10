package no.fortedigital.models.event

import kotlinx.serialization.Serializable

// Generic data class used to send messages on the message queue using the same structure
@Serializable
data class EventMessage<T>(val type: String, val payload: T)