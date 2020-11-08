package io.species.analyzer.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Specie not found")
public class SpecieNotFoundException extends SpecieException {

    public SpecieNotFoundException(String message) {
        super(message);
    }
}
