package io.species.analyzer.domain.event;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class DomainEventNotifier implements EventNotifier<DomainEvent> {

    private final Map<EventType, List<EventListener>> listeners = new EnumMap<>(EventType.class);

    public DomainEventNotifier(final Map<EventType, List<EventListener>> listeners) {
        this.listeners.putAll(listeners);
    }

    @Override
    public void notify(final DomainEvent event) {
        listeners.getOrDefault(event.eventType(), List.of()).forEach(eventListener -> eventListener.onEvent(event));
    }
}
