package io.species.analyzer.infrastructure.command;

@FunctionalInterface
public interface Command<T,R> {

    R execute(final T argument);
}
