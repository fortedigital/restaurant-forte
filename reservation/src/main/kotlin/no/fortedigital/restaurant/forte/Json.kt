package no.fortedigital.restaurant.forte

import kotlinx.serialization.json.Json

internal val jsonFormatter = Json {
    ignoreUnknownKeys = true
}