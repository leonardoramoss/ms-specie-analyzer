package io.species.analyzer.infrastructure.serialization.serializer;

import io.species.analyzer.infrastructure.exception.handler.SpecieExceptionData;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SpecieExceptionDataSerializer extends AbstractSerializer<SpecieExceptionData> {

    @Override
    public void serialize(final SpecieExceptionData value, final SerializationLabel serializationLabel, final JsonWriter jsonWriter) throws IOException {
        final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        jsonWriter.writeStartObject(serializationLabel);
        jsonWriter.writeStringField(SpecieExceptionDataLabels.CODE, value.getHttpStatus().toString());
        jsonWriter.writeStringField(SpecieExceptionDataLabels.TIMESTAMP, formatter.format(value.getTimestamp()));
        jsonWriter.writeStringField(SpecieExceptionDataLabels.CAUSE, value.getCause());
        jsonWriter.writeStringField(SpecieExceptionDataLabels.MESSAGE, value.getMessage());
        jsonWriter.writeEndObject();
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
