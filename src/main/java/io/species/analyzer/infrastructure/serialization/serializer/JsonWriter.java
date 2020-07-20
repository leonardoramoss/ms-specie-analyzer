package io.species.analyzer.infrastructure.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public class JsonWriter {

    private final JsonGenerator generator;

    public JsonWriter(final JsonGenerator generator) {
        this.generator = generator;
    }

    public void writeStartObject(final SerializationLabel serializationLabel) throws IOException {
        if (Objects.isNull(serializationLabel) || ObjectUtils.isEmpty(serializationLabel.label())) {
            generator.writeStartObject();
        } else {
            generator.writeObjectFieldStart(serializationLabel.label());
        }
    }

    public void writeNumberField(final SerializationLabel serializationLabel, final BigDecimal value) throws IOException {
        generator.writeNumberField(serializationLabel.label(), value);
    }

    public void writeNumberField(final SerializationLabel serializationLabel, final Long value) throws IOException {
        generator.writeNumberField(serializationLabel.label(), value);
    }

    public void writeStringField(final SerializationLabel serializationLabel, final String value) throws IOException {
        if (!ObjectUtils.isEmpty(value)) {
            generator.writeStringField(serializationLabel.label(), value);
        }
    }

    public void writeEndObject() throws IOException {
        generator.writeEndObject();
    }
}

