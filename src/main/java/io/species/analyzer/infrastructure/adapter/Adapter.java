package io.species.analyzer.infrastructure.adapter;

@FunctionalInterface
public interface Adapter<T, R> {

    R adapt(final T argument);
}
