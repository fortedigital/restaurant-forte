package no.fortedigital.models.event

interface EventBusProducer {
    suspend fun produce(topic: String, message: String, key: String? = null)
}

interface EventBusConsumer {
    suspend fun consume(topic: String, handler: suspend (List<Packet>) -> Unit)
}

interface EventBus : EventBusProducer, EventBusConsumer