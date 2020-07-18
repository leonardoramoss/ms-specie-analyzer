package io.species.analyzer.infrastructure.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

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

    public void writeNumberField(final Enum<?> fieldName, final Integer value) throws IOException {
        if(!ObjectUtils.isEmpty(value))
            jsonGenerator.writeNumberField(fieldName.toString(), value);
    }

    public void writeStringField(final Enum<?> fieldName, final String value) throws IOException {
        if (!ObjectUtils.isEmpty(value))
            jsonGenerator.writeStringField(fieldName.toString(), value);
    }

    public void writeEndObject() throws IOException {
        jsonGenerator.writeEndObject();
    }
}
