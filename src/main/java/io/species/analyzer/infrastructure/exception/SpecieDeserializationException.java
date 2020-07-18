package io.species.analyzer.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Failed deserialization")
public class SpecieDeserializationException extends SpecieException {

    public SpecieDeserializationException(final String message) {
        super(message);
    }
}
