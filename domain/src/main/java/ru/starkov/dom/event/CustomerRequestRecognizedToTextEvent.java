package ru.starkov.dom.event;

import ru.starkov.dom.entity.identifier.CustomerRequestId;
import ru.starkov.common.mark.DomainEvent;

public record CustomerRequestRecognizedToTextEvent(CustomerRequestId customerRequestId) implements DomainEvent {
}
