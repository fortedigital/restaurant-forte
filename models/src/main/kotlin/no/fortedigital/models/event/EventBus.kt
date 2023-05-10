package no.fortedigital.models.event

interface EventBusProducer<MessageType> {
    fun produce(topic: String, message: EventMessage<MessageType>)
}

interface EventBusConsumer<MessageType> {
    fun consume(topic: String, handler: (List<EventMessage<MessageType>>) -> Unit)
}

interface EventBus<MessageType> : EventBusProducer<MessageType>, EventBusConsumer<MessageType>