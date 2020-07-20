package io.species.analyzer.infrastructure.retrieve;

@FunctionalInterface
public interface Retriever<T, R> {

    R retrieve(final T argument);
}
