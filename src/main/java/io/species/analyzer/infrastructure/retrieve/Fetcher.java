package io.species.analyzer.infrastructure.retrieve;

@FunctionalInterface
public interface Fetcher<T, R> {

    R fetch(final T argument);
}
