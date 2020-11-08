package io.species.analyzer.infrastructure.auditing.filter;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class LoggingMessageBuilder {

    private final StringBuilder messageBuilder = new StringBuilder();

    /**
     * @param value
     *
     * @return
     */
    public LoggingMessageBuilder append(final Object value) {
        if (value != null) {
            messageBuilder.append(value);
        }
        return this;
    }

    /**
     * @param label
     * @param value
     *
     * @return
     */
    public LoggingMessageBuilder append(final String label, final Object value) {
        if (value != null) {
            messageBuilder.append("[").append(label).append(": ").append(value).append("] ");
        }
        return this;
    }

    /**
     * @param label
     * @param value
     * @param sufix
     *
     * @return
     */
    public LoggingMessageBuilder append(final String label, final Object value, final String sufix) {
        if (value != null) {
            messageBuilder.append("[").append(label).append(": ").append(value).append(" ").append(sufix).append("] ");
        }
        return this;
    }

    /**
     * @param label
     * @param value
     *
     * @return
     */
    public LoggingMessageBuilder append(final String label, final Map<String, String> value) {
        if (value != null && !value.isEmpty()) {
            messageBuilder.append("[").append(label).append(": ").append(value).append("] ");
        }
        return this;
    }

    /**
     * @param label
     * @param status
     *
     * @return
     */
    public LoggingMessageBuilder append(final String label, final HttpStatus status) {
        if (status != null) {
            messageBuilder.append("[").append(label).append(": ").append(status.value()).append(" - ").append(status.getReasonPhrase()).append("] ");
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return messageBuilder.toString();
    }

    /**
     * Return 'compiled' phrase to be used on auditing feature
     *
     * @return full phrase to perform log
     */
    public String build() {
        return toString();
    }
}
