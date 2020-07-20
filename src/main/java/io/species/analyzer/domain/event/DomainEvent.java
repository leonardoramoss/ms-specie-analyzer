package io.species.analyzer.domain.event;

import java.util.EventObject;
import java.util.Optional;

public abstract class DomainEvent<T> extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public DomainEvent(final T source) {
        super(Optional.ofNullable(source));
    }


    @Override
    public T getSource() {
        final Optional<T> source = (Optional<T>) super.getSource();
        return source.orElse(null);
    }

    public abstract EventType eventType();
}
