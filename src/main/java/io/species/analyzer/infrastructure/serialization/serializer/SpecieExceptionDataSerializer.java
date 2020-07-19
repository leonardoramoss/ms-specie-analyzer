package io.species.analyzer.infrastructure.serialization.serializer;

import io.species.analyzer.infrastructure.exception.handler.SpecieExceptionData;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SpecieExceptionDataSerializer extends AbstractSerializer<SpecieExceptionData> {

    @Override
    public void serialize(final SpecieExceptionData value) throws IOException {
        final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.writeStartObject();
        this.writeNumberField(SpecieExceptionDataLabels.CODE, value.getHttpStatus().value());
        this.writeStringField(SpecieExceptionDataLabels.TIMESTAMP, formatter.format(value.getTimestamp()));
        this.writeStringField(SpecieExceptionDataLabels.CAUSE, value.getCause());
        this.writeStringField(SpecieExceptionDataLabels.MESSAGE, value.getMessage());
        this.writeEndObject();
    }

    private enum SpecieExceptionDataLabels implements SerializationLabel {

        CODE("code"),
        CAUSE("cause"),
        TIMESTAMP("timestamp"),
        MESSAGE("message");

        private final String label;

        SpecieExceptionDataLabels(final String label) {
            this.label = label;
        }

        @Override
        public String label() {
            return label;
        }
    }
}
