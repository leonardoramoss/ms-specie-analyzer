package io.species.analyzer.infrastructure.exception;

public class SpecieException extends RuntimeException {

    public SpecieException(final String message) {
        super((message));
    }

    public SpecieException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
