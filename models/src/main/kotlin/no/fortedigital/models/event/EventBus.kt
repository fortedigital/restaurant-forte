package no.fortedigital.models.event

interface EventBusProducer<MessageType: Identifiable> {
    suspend fun produce(topic: String, message: EventMessage<MessageType>)
}

interface EventBusConsumer<MessageType: Identifiable> {
    suspend fun consume(topic: String, handler: (List<EventMessage<MessageType>>) -> Unit)
}

interface EventBus<MessageType: Identifiable> : EventBusProducer<MessageType>, EventBusConsumer<MessageType>