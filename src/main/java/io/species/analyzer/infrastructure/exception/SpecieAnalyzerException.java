package io.species.analyzer.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Analyzer not found for specified specie.")
public class SpecieAnalyzerException extends SpecieException {

    public SpecieAnalyzerException(final String message) {
        super(message);
    }
}
