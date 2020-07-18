package io.species.analyzer.infrastructure.exception.handler;

import io.species.analyzer.infrastructure.exception.SimianException;
import io.species.analyzer.infrastructure.exception.SpecieException;
import io.species.analyzer.infrastructure.exception.SpecieValidationException;
import io.species.analyzer.infrastructure.exception.SpecieDeserializationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SpecieValidationException.class)
    protected ResponseEntity<SpecieExceptionData> handleSpecieValidationException(final SpecieValidationException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(SpecieDeserializationException.class)
    protected ResponseEntity<SpecieExceptionData> handleSpecieDeserializationException(final SpecieDeserializationException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(SimianException.class)
    protected ResponseEntity<SpecieExceptionData> handleSimianException(final SimianException exception) {
        return handleException(exception);
    }

    private ResponseEntity<SpecieExceptionData> handleException(final SpecieException exception) {
        final ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        final SpecieExceptionData exceptionData = SpecieExceptionData.of(annotation, exception);
        return new ResponseEntity<>(exceptionData, annotation.code());
    }
}
