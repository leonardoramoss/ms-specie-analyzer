package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.species.analyzer.infrastructure.exception.SpecieException;

public abstract class AbstractDeserializer<T> extends JsonDeserializer<T> {

    private final DeserializeOperations operations = new DeserializeOperations();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public T deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) {
        try {
            final var codec = jsonParser.getCodec();
            final var jsonNode = (JsonNode) codec.readTree(jsonParser);
            return deserialize(jsonNode);
        } catch (Exception e) {
            throw new SpecieException("", e);
        }
    }

    public abstract T deserialize(final JsonNode jsonNode);

    protected <R> R extractAs(final JsonNode jsonNode, final Enum<?> field, final Class<R> type) {
        return objectMapper.convertValue(extractAsJsonNode(jsonNode, field), type);
    }

    protected JsonNode extractAsJsonNode(final JsonNode node, final Enum<?> fieldName) {
        return operations.extractAsJsonNode(node, fieldName);
    }
}
