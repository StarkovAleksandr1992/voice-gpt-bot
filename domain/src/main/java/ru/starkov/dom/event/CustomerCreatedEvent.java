package ru.starkov.dom.event;

import ru.starkov.dom.entity.identifier.CustomerId;
import ru.starkov.common.mark.DomainEvent;
public record CustomerCreatedEvent(CustomerId customerId) implements DomainEvent {
}
