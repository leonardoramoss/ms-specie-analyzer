package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.species.analyzer.infrastructure.exception.SpecieDeserializationException;
import io.species.analyzer.infrastructure.exception.SpecieException;

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

    protected <R> R extractAs(final JsonNode jsonNode, final Enum<?> field, final Class<R> type) {
        return objectMapper.convertValue(extractAsJsonNode(jsonNode, field), type);
    }

    protected JsonNode extractAsJsonNode(final JsonNode node, final Enum<?> fieldName) {
        return hasNonNull(node, fieldName) ? node.findValue(fieldName.toString()) : null;
    }

    private boolean hasNonNull(final JsonNode node, final Enum<?> fieldName) {
        return has(node, fieldName) && node.hasNonNull(fieldName.toString());
    }

    private boolean has(final JsonNode node, final Enum<?> fieldName) {
        return !Objects.isNull(node) && node.has(fieldName.toString());
    }
}
