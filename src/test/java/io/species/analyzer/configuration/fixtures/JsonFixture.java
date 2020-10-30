package io.species.analyzer.configuration.fixtures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONValue;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonFixture {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String loadJsonFile(final String path) {
        return JSONValue.parse(
            new InputStreamReader(
                JsonFixture.class.getResourceAsStream(String.format("/%s", path)),
                StandardCharsets.UTF_8
            )
        ).toString();
    }

    public static JsonNode loadJsonFileAsJsonNode(String path) {
        try {
            return objectMapper.readTree(loadJsonFile(path));
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
