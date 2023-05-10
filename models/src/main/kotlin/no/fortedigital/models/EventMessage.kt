package no.fortedigital.models

import kotlinx.serialization.Serializable

@Serializable
data class EventMessage<T>(val type: String, val payload: T)
