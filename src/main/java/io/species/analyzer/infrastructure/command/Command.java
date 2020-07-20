package io.species.analyzer.infrastructure.command;

@FunctionalInterface
public interface Command<T> {

    void execute(final T argument);
}
