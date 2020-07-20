package io.species.analyzer.infrastructure.exception.handler;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.species.analyzer.infrastructure.exception.SpecieException;
import io.species.analyzer.infrastructure.serialization.serializer.SpecieExceptionDataSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@JsonSerialize(using = SpecieExceptionDataSerializer.class)
public class SpecieExceptionData {

    private final HttpStatus httpStatus;

    private final String cause;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private SpecieExceptionData(final HttpStatus httpStatus, final String cause, final String message) {
        this.httpStatus = httpStatus;
        this.cause = cause;
        this.message = message;
    }

    public static SpecieExceptionData of(final ResponseStatus responseStatus, final SpecieException specieException) {
        return new SpecieExceptionData(responseStatus.code(), responseStatus.reason(), specieException.getMessage());
    }

    public static SpecieExceptionData of(final HttpStatus httpStatus, final String reason, final String message) {
        return new SpecieExceptionData(httpStatus, reason, message);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
