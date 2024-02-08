package ru.starkov.common.event;

import lombok.Getter;
import ru.starkov.common.mark.DomainEvent;

@Getter
public class ResultWithEvent<T, K extends DomainEvent> {

    private final T value;
    private final K event;

    public static <T, K extends DomainEvent> ResultWithEvent<T, K> of(T result, K event) {
        return new ResultWithEvent<>(result, event);
    }

    private ResultWithEvent(T value, K event) {
        this.value = value;
        this.event = event;
    }
}
