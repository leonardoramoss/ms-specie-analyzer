package io.species.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import io.species.analyzer.infrastructure.AbstractIntegrationTests;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.awaitility.Awaitility.await;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpeciesAnalysisIntegrationTests extends AbstractIntegrationTests {

    private static final String SIMIAN_ENDPOINT = "/v1/simian";

    private static final String SPECIES_ANALYSIS_TABLE = "SPECIE.SPECIES_ANALYSIS";
    private static final String SPECIES_ANALYSIS_COUNTER_TABLE = "SPECIE.SPECIES_ANALYSIS_COUNTER";

    @Autowired
    private SpeciesAnalysisRepository speciesAnalysisRepository;

    @Test
    @SqlGroup({
        @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHorizontalSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/mock_simian_horizontal_payload.json"), status().isOk());
        verifyDatabase("expected_simian_horizontal_specie.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidVerticalSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/mock_simian_vertical_payload.json"), status().isOk());
        verifyDatabase("expected_simian_vertical_species.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidDiagonalSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/mock_simian_diagonal_payload.json"), status().isOk());
        verifyDatabase("expected_simian_diagonal_species.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidReversedDiagonalSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/mock_simian_reversed_diagonal_payload.json"), status().isOk());
        verifyDatabase("expected_simian_reversed_diagonal_species.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHorizontalSimianDna_whenPerformPostAnAlreadySavedDna_shouldBeReturnFromDatabaseAndStatusOk() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/mock_simian_horizontal_payload.json"), status().isOk());

        verifyDatabase("expected_simian_horizontal_specie.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");

        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/mock_simian_horizontal_payload.json"), status().isOk());

        verifyDatabase("expected_simian_horizontal_specie.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
        @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHumanDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusForbidden() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/mock_human_payload.json"), status().isForbidden());
        verifyDatabase("expected_human_specie.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_human_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithNotAllowedCharacter_whenPerformPost_shouldBeReturnStatusBadRequest() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/invalid/mock_invalid_not_allowed_character_payload.json"), status().isBadRequest());
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithEmptyDNA_whenPerformPost_shouldBeReturnStatusBadRequest() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/invalid/mock_invalid_empty_dna_payload.json"), status().isBadRequest());
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }
    
    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithoutDNALabel_whenPerformPost_shouldBeReturnStatusBadRequest() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/invalid/mock_invalid_without_dna_label_payload.json"), status().isBadRequest());
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithNullDNA_whenPerformPost_shouldBeReturnStatusBadRequest() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, "{ \"dna\": }", status().isBadRequest());
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithNotANxNDNASequence_whenPerformPost_shouldBeReturnStatusBadRequest() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/invalid/mock_invalid_not_a_NxN_payload.json"), status().isBadRequest());
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithLessNxNDNASequenceAllowed_whenPerformPost_shouldBeReturnStatusBadRequest() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, getJsonFileAsString("mock/invalid/mock_invalid_with_less_NxN_allowed_payload.json"), status().isBadRequest());
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenBatchOfValidPayloads_whenPerformPost_shouldBeReturnStatusOkForSimiansAndForbiddenForHuman() throws JSONException {
        final var expectations = Map.of(SpeciesIdentifier.HUMAN, status().isForbidden(), SpeciesIdentifier.SIMIAN, status().isOk());
        final var mockPayloads = getPayloads("mock/mock_simian_payloads.json", "mock/mock_human_payloads.json");

        mockPayloads.parallelStream()
            .forEach(mockPayload -> {
                final var speciesIdentifier = SpeciesIdentifier.valueOf(mockPayload.get("species").asText());
                final var jsonPayload = mockPayload.get("payload");
                performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, jsonPayload.toString(), expectations.get(speciesIdentifier));
            });

        await().pollInterval(200, TimeUnit.MILLISECONDS)
                .timeout(500, TimeUnit.MILLISECONDS)
                .until(() -> speciesAnalysisRepository.count(), Matchers.equalTo(150L));

        final var querySpeciesAnalysis = "SELECT * FROM " + SPECIES_ANALYSIS_TABLE + " ORDER BY UUID";
        final var querySpeciesAnalysisCounter = "SELECT * FROM " + SPECIES_ANALYSIS_COUNTER_TABLE + " ORDER BY SPECIE";

        verifyDatabaseWithQuery("fullflow/expected_valid_human_and_simian_payloads.xml", SPECIES_ANALYSIS_TABLE, querySpeciesAnalysis, "ANALYZED_AT");
        verifyDatabaseWithQuery("fullflow/expected_species_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE, querySpeciesAnalysisCounter);

        final var mvcResult = performGetAndExpect("/v1/stats", status().isOk());
        assertEquals(getJsonFileAsString("expected/response/stats/fullflow/expected_fullflow_stats.json"), getMvcResultAsString(mvcResult), true);
    }

    private List<JsonNode> getPayloads(final String ... pathsMockPayloads) {
        return Arrays.stream(pathsMockPayloads)
                .map(this::getJsonNodeFromJsonFile)
                .flatMap(payload -> StreamSupport.stream(payload.spliterator(), false))
                .collect(Collectors.toList());
    }
}
