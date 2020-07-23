package io.species.analyzer.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONValue;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class AbstractTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected String getJsonFileAsString(final String path) {
        final var resourceAsStream = this.getClass().getResourceAsStream(String.format("/%s", path));
        return JSONValue.parse(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)).toString();
    }

    protected JsonNode getJsonNodeFromJsonFile(final String path) {
        return getJsonNodeFromString(getJsonFileAsString(path));
    }

    protected JsonNode getJsonNodeFromString(final String string)  {
        try {
            return objectMapper.readTree(string);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
