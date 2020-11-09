package io.species.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import io.species.analyzer.configuration.fixtures.DatabaseAssertion;
import io.species.analyzer.configuration.fixtures.DatabaseFixture;
import io.species.analyzer.configuration.fixtures.JsonFixture;
import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.species.analyzer.configuration.fixtures.JsonFixture.loadJsonFile;
import static org.awaitility.Awaitility.await;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
public class FullFlowIntegrationTests implements DatabaseAssertion {

    @Autowired
    private DatabaseFixture databaseFixture;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private SpeciesAnalysisRepository speciesAnalysisRepository;

    private static final String SPECIES_ANALYSIS_TABLE = "SPECIE.SPECIES_ANALYSIS";
    private static final String SPECIES_ANALYSIS_COUNTER_TABLE = "SPECIE.SPECIES_ANALYSIS_COUNTER";

    private static final String SIMIAN_ENDPOINT = "/v1/analyze/simian";

    @Override
    public void verifyDatabase(String fileName, String tableName, String query, String... ignoredColumns) {
        databaseFixture.verifyDatabase(fileName, tableName, query, ignoredColumns);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenBatchOfValidPayloads_whenPerformPost_shouldBeReturnStatusOkForSimiansAndForbiddenForHuman() throws JSONException, UnsupportedEncodingException {
        final var expectations = Map.of(Specie.HUMAN, status().isForbidden(), Specie.SIMIAN, status().isOk());
        final var mockPayloads = getPayloads("mock/mock_simian_payloads.json", "mock/mock_human_payloads.json");

        mockPayloads.parallelStream()
                .forEach(mockPayload -> {
                    final var speciesIdentifier = Specie.valueOf(mockPayload.get("species").asText());
                    final var jsonPayload = mockPayload.get("payload");
                    performPostWithPayloadAndExpect(jsonPayload.toString(), expectations.get(speciesIdentifier));
                });

        await().pollInterval(200, TimeUnit.MILLISECONDS)
                .timeout(500, TimeUnit.MILLISECONDS)
                .until(() -> speciesAnalysisRepository.count(), Matchers.equalTo(150L));

        final var querySpeciesAnalysis = "SELECT * FROM " + SPECIES_ANALYSIS_TABLE + " ORDER BY UUID";
        final var querySpeciesAnalysisCounter = "SELECT * FROM " + SPECIES_ANALYSIS_COUNTER_TABLE + " ORDER BY SPECIE";

        verifyDatabase("fullflow/expected_valid_human_and_simian_payloads.xml", SPECIES_ANALYSIS_TABLE, querySpeciesAnalysis, "ANALYZED_AT");
        verifyDatabase("fullflow/expected_species_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE, querySpeciesAnalysisCounter);

        final var mvcResult = performGetAndExpect(status().isOk());
        assertEquals(loadJsonFile("expected/response/stats/fullflow/expected_fullflow_stats.json"), mvcResult.getResponse().getContentAsString(), true);
    }

    private void performPostWithPayloadAndExpect(final String jsonPayload, final ResultMatcher expected) {
        try {
            mockMvc.perform(
                    post(FullFlowIntegrationTests.SIMIAN_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(expected)
                    .andReturn();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private MvcResult performGetAndExpect(final ResultMatcher expected) {
        try {
            return mockMvc.perform(get("/v1/stats")).andExpect(expected).andReturn();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private List<JsonNode> getPayloads(final String ... pathsMockPayloads) {
        return Arrays.stream(pathsMockPayloads)
                .map(JsonFixture::loadJsonFileAsJsonNode)
                .flatMap(payload -> StreamSupport.stream(payload.spliterator(), false))
                .collect(Collectors.toList());
    }
}
