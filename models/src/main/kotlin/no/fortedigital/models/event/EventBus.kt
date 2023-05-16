package no.fortedigital.models.event

interface EventBusProducer<MessageType: Identifiable> {
    suspend fun produce(topic: String, message: String, key: String? = null)
}

interface EventBusConsumer<MessageType: Identifiable> {
    suspend fun consume(topic: String, handler: (List<EventMessage<MessageType>>) -> Unit)
}

interface EventBus<MessageType: Identifiable> : EventBusProducer<MessageType>, EventBusConsumer<MessageType>