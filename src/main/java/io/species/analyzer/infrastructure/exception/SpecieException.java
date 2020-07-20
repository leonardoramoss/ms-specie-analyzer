package io.species.analyzer.infrastructure.exception;

public class SpecieException extends RuntimeException {

    protected SpecieException(final String message) {
        super((message));
    }
}
