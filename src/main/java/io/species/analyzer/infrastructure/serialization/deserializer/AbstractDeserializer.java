package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.species.analyzer.infrastructure.exception.SpecieDeserializationException;
import io.species.analyzer.infrastructure.exception.SpecieException;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;

import java.util.Objects;

public abstract class AbstractDeserializer<T> extends JsonDeserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public T deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) {

        JsonNode jsonNode = JsonNodeFactory.instance.objectNode();

        try {
            final var codec = jsonParser.getCodec();
            jsonNode = codec.readTree(jsonParser);
            return deserialize(jsonNode);
        } catch (final SpecieException e) {
            throw e;
        } catch (final Exception e) {
            throw new SpecieDeserializationException(String.format("Cannot deserializer %s. Is not a valid payload.", jsonNode));
        }
    }

    public abstract T deserialize(final JsonNode jsonNode);

    protected <R> R readFieldAs(final JsonNode jsonNode, final SerializationLabel serializationLabel, final Class<R> type) {
        return objectMapper.convertValue(readJsonNodeField(jsonNode, serializationLabel), type);
    }

    protected JsonNode readJsonNodeField(final JsonNode node, final SerializationLabel serializationLabel) {
        return hasNonNull(node, serializationLabel) ? node.findValue(serializationLabel.label()) : null;
    }

    private boolean hasNonNull(final JsonNode node, final SerializationLabel serializationLabel) {
        return has(node, serializationLabel) && node.hasNonNull(serializationLabel.label());
    }

    private boolean has(final JsonNode node, final SerializationLabel serializationLabel) {
        return !Objects.isNull(node) && node.has(serializationLabel.label());
    }
}
