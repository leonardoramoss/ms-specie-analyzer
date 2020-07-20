package io.species.analyzer.infrastructure.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;

import java.io.IOException;

public abstract class AbstractSerializer<T> extends JsonSerializer<T> {

    @Override
    public void serialize(final T argument, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        final var jsonWriter = new JsonWriter(jsonGenerator);
        serialize(argument, jsonWriter);
    }

    public abstract void serialize(final T argument, final SerializationLabel serializationLabel, final JsonWriter jsonWriter) throws IOException;

    public void serialize(final T argument, final JsonWriter jsonWriter) throws IOException {
        serialize(argument, null, jsonWriter);
    }
}
