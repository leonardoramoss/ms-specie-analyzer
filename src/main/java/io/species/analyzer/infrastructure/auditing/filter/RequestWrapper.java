package io.species.analyzer.infrastructure.auditing.filter;

import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.UUID;

public class RequestWrapper extends HttpServletRequestWrapper {

    private final UUID uuid;

    /**
     * Constructor for {@link RequestWrapper}
     *
     * @param request object with request information
     * @param uuid is unique identification from current request
     */
    public RequestWrapper(final HttpServletRequest request, final UUID uuid) {
        super(request);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * @return
     */
    public byte[] getContentAsByteArray() {
        final var wrapper = (ContentCachingRequestWrapper) getRequest();
        return wrapper.getContentAsByteArray();
    }
}
