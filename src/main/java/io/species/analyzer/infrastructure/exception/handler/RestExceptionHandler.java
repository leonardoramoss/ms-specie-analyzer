package io.species.analyzer.infrastructure.exception.handler;

import io.species.analyzer.infrastructure.exception.SimianException;
import io.species.analyzer.infrastructure.exception.SpecieDeserializationException;
import io.species.analyzer.infrastructure.exception.SpecieException;
import io.species.analyzer.infrastructure.exception.SpecieValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

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

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<SpecieExceptionData> handleException(final Exception exception, final HttpServletRequest httpServletRequest) {
        final var uri = httpServletRequest.getRequestURI();
        final var rootCause = exception.getCause();
        final var exceptionData = SpecieExceptionData.of(HttpStatus.INTERNAL_SERVER_ERROR, exception.getCause().toString(), "Opss!");
        LOG.error(String.format("URI %s - %s - Root Cause: %s", uri, exception.getMessage(), rootCause != null ? rootCause.getMessage() : ""));
        return new ResponseEntity<>(exceptionData, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<SpecieExceptionData> handleException(final SpecieException exception) {
        final var annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        final var exceptionData = SpecieExceptionData.of(annotation, exception);
        return new ResponseEntity<>(exceptionData, annotation.code());
    }
}
