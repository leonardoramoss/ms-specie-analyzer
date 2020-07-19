package io.species.analyzer.infrastructure.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;

public abstract class AbstractSerializer<T> extends JsonSerializer<T> {

    private JsonGenerator jsonGenerator;

    @Override
    public void serialize(final T argument, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        this.jsonGenerator = jsonGenerator;
        serialize(argument);
    }

    public abstract void serialize(final T value) throws IOException;

    public void writeStartObject() throws IOException {
        jsonGenerator.writeStartObject();
    }

    public void writeNumberField(final SerializationLabel serializationLabel, final Integer value) throws IOException {
        this.writeNumberField(serializationLabel.label(), value);
    }

    public void writeNumberField(final SerializationLabel serializationLabel, final BigDecimal value) throws IOException {
        this.writeNumberField(serializationLabel.label(), value);
    }

    public void writeStringField(final SerializationLabel serializationLabel, final String value) throws IOException {
        this.writeStringField(serializationLabel.toString(), value);
    }

    public void writeNumberField(final SerializationLabel serializationLabel, final Long value) throws IOException {
        if(!ObjectUtils.isEmpty(value))
            jsonGenerator.writeNumberField(serializationLabel.label(), value);
    }

    public void writeNumberField(final String fieldName, final Integer value) throws IOException {
        if(!ObjectUtils.isEmpty(value))
            jsonGenerator.writeNumberField(fieldName, value);
    }

    public void writeNumberField(final String fieldName, final BigDecimal value) throws IOException {
        if(!ObjectUtils.isEmpty(value))
            jsonGenerator.writeNumberField(fieldName, value);
    }

    public void writeStringField(final String fieldName, final String value) throws IOException {
        if (!ObjectUtils.isEmpty(value))
            jsonGenerator.writeStringField(fieldName, value);
    }

    public void writeEndObject() throws IOException {
        jsonGenerator.writeEndObject();
    }
}
