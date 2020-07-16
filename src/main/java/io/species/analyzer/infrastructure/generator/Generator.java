package io.species.analyzer.infrastructure.generator;

@FunctionalInterface
public interface Generator<T, R> {

    R generate(final T argument);
}
