package io.species.analyzer.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "DNA not matches as expected")
public class SpecieAnalysisNotMatchesException extends SpecieAnalyzerException {

    public SpecieAnalysisNotMatchesException(String message) {
        super(message);
    }
}
