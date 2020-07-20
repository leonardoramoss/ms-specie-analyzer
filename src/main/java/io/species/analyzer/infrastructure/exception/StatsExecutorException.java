package io.species.analyzer.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Stats executor not found")
public class StatsExecutorException extends SpecieException {

    public StatsExecutorException(final String message) {
        super(message);
    }
}
