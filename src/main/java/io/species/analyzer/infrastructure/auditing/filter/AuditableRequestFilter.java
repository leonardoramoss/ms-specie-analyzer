package io.species.analyzer.infrastructure.auditing.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class AuditableRequestFilter extends OncePerRequestFilter {

    private static final String REQUEST_PREFIX = "API REQUEST - ";

    private static final String RESPONSE_PREFIX = "API RESPONSE - ";

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest httpServletRequest,
                                    @NonNull final HttpServletResponse httpServletResponse,
                                    final FilterChain filterChain) throws IOException, ServletException {

        final var startTime = System.currentTimeMillis();

        final var uuid = UUID.fromString(MDC.get(IdentificationRequestFilter.UUID_ATTRIBUTE_VALUE));
        final var contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);

        final var requestWrapper = new RequestWrapper(contentCachingRequestWrapper, uuid);
        final var responseWrapper = new ResponseWrapper(httpServletResponse, uuid);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            logRequest(requestWrapper);
            final var elapsedTime = System.currentTimeMillis() - startTime;
            logResponse(responseWrapper, elapsedTime);
        }
    }

    private void logRequest(final RequestWrapper requestWrapper) {

        final var loggingMessage = new LoggingMessageBuilder();

        loggingMessage.append(REQUEST_PREFIX)
                .append("REQUEST ID", requestWrapper.getUuid())
                .append("HTTP METHOD", requestWrapper.getMethod())
                .append("CONTENT TYPE", requestWrapper.getContentType())
                .append("URL", getRequestedURL(requestWrapper))
                .append("REQUEST PARAMS", getRequestParams(requestWrapper))
                .append("REQUESTS HEADERS", getRequestHeaders(requestWrapper));

        try {
            final var payload = new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
            if(!payload.isEmpty())
                loggingMessage.append("PAYLOAD", payload);
        } catch (final UnsupportedEncodingException e) {
            final var failed  = "Failed to parse request payload";
            loggingMessage.append("PAYLOAD", failed);
            logger.warn(failed, e);
        }

        logger.info(loggingMessage.build());
    }

    private void logResponse(final ResponseWrapper responseWrapper, final long elapsedTime) {

        final var loggingMessage = new LoggingMessageBuilder();

        loggingMessage.append(RESPONSE_PREFIX)
                .append("REQUEST ID", responseWrapper.getUuid())
                .append("HTTP STATUS", HttpStatus.valueOf(responseWrapper.getStatus()))
                .append("PROCESSING TIME", elapsedTime, "ms");

        try {
            final var payload = new String(responseWrapper.toByteArray(), responseWrapper.getCharacterEncoding());
            if(!payload.isEmpty())
                loggingMessage.append("PAYLOAD", payload);
        } catch (final UnsupportedEncodingException e) {
            final var failed  = "Failed to parse response payload";
            loggingMessage.append("PAYLOAD", failed);
            logger.warn(failed, e);
        }

        logger.info(loggingMessage.build());
    }

    private String getRequestedURL(final RequestWrapper request) {

        final var urlBuilder = new StringBuilder();

        urlBuilder.append(request.getRequestURI());

        if (request.getQueryString() != null) {
            urlBuilder.append('?').append(request.getQueryString());
        }
        return urlBuilder.toString();
    }

    private Map<String, String> getRequestHeaders(final HttpServletRequest request) {

        final var typeSafeRequestMap = new HashMap<String, String>();
        final var requestHeaderNames = request.getHeaderNames();

        while (requestHeaderNames.hasMoreElements()) {
            final var requestHeaderName = requestHeaderNames.nextElement();
            final var requestHeaderValue = request.getHeader(requestHeaderName);
            typeSafeRequestMap.put(requestHeaderName, requestHeaderValue);
        }
        return Collections.unmodifiableMap(typeSafeRequestMap);
    }

    private Map<String, String> getRequestParams(final HttpServletRequest request) {

        final var typeSafeRequestMap = new HashMap<String, String>();
        final var requestParameterNames = request.getParameterNames();

        while (requestParameterNames.hasMoreElements()) {
            final var requestParamName = requestParameterNames.nextElement();
            final var requestParamValue = request.getParameter(requestParamName);
            typeSafeRequestMap.put(requestParamName, requestParamValue);
        }
        return Collections.unmodifiableMap(typeSafeRequestMap);
    }
}
