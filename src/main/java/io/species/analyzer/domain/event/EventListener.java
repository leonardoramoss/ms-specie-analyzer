package io.species.analyzer.domain.event;

@FunctionalInterface
public interface EventListener<T> {

    void onEvent(final T event);
}
