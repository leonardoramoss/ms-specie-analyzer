package io.species.analyzer.infrastructure.auditing.filter;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Order(1)
@Component
public class IdentificationRequestFilter extends OncePerRequestFilter {

    public static final String UUID_ATTRIBUTE_VALUE = "UUID";

    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest,
                                    final HttpServletResponse httpServletResponse,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            ThreadContext.put(UUID_ATTRIBUTE_VALUE, UUID.randomUUID().toString());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            MDC.remove(UUID_ATTRIBUTE_VALUE);
        }
    }
}
