package io.species.analyzer.domain.event;

@FunctionalInterface
public interface EventNotifier<T> {

    void notify(final T argument);
}
