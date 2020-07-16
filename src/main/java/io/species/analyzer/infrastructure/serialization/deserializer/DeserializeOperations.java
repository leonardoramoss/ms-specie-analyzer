package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

public class DeserializeOperations {

    public JsonNode extractAsJsonNode(final JsonNode node, final Enum<?> fieldName) {
        return this.hasNonNull(node, fieldName) ? node.findValue(fieldName.toString()) : null;
    }

    public boolean hasNonNull(final JsonNode node, final Enum<?> fieldName) {
        return this.has(node, fieldName) && node.hasNonNull(fieldName.toString());
    }

    public boolean has(final JsonNode node, final Enum<?> fieldName) {
        return !Objects.isNull(node) && node.has(fieldName.toString());
    }
}
