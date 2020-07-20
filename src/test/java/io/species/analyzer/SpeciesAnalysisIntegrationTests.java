package io.species.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import io.species.analyzer.infrastructure.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpeciesAnalysisIntegrationTests extends AbstractIntegrationTests {

    private static final String SIMIAN_ENDPOINT = "/v1/simian";

    private static final String SPECIES_ANALYSIS_TABLE = "SPECIE.SPECIES_ANALYSIS";
    private static final String SPECIES_ANALYSIS_COUNTER_TABLE = "SPECIE.SPECIES_ANALYSIS_COUNTER";

    @Test
    @SqlGroup({
        @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHorizontalSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() throws Exception {
        mockMvc.perform(
                    post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_horizontal_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_horizontal_specie.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidVerticalSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_vertical_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_vertical_species.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidDiagonalSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_diagonal_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_diagonal_species.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidReversedDiagonalSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_reversed_diagonal_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_reversed_diagonal_species.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHorizontalSimianDna_whenPerformPostAnAlreadySavedDna_shouldBeReturnFromDatabaseAndStatusOk() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_horizontal_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_horizontal_specie.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");

        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_horizontal_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_horizontal_specie.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");

        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
        @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHumanDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_human_payload.json")))
                .andExpect(status().isForbidden());

        verifyDatabase("expected_human_specie.xml", SPECIES_ANALYSIS_TABLE, "ANALYZED_AT");

        verifyDatabase("expected_human_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithNotAllowedCharacter_whenPerformPost_shouldBeReturnStatusBadRequest() throws Exception {
        sendPostAndValidatedBadRequestResponse(getJsonFileAsString("mock/invalid/mock_invalid_not_allowed_character_payload.json"));
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithEmptyDNA_whenPerformPost_shouldBeReturnStatusBadRequest() throws Exception {
        sendPostAndValidatedBadRequestResponse(getJsonFileAsString("mock/invalid/mock_invalid_empty_dna_payload.json"));
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }
    
    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithoutDNALabel_whenPerformPost_shouldBeReturnStatusBadRequest() throws Exception {
        sendPostAndValidatedBadRequestResponse(getJsonFileAsString("mock/invalid/mock_invalid_without_dna_label_payload.json"));
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithNullDNA_whenPerformPost_shouldBeReturnStatusBadRequest() throws Exception {
        sendPostAndValidatedBadRequestResponse("{ \"dna\": }");
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithNotANxNDNASequence_whenPerformPost_shouldBeReturnStatusBadRequest() throws Exception {
        sendPostAndValidatedBadRequestResponse(getJsonFileAsString("mock/invalid/mock_invalid_not_a_NxN_payload.json"));
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithLessNxNDNASequenceAllowed_whenPerformPost_shouldBeReturnStatusBadRequest() throws Exception {
        sendPostAndValidatedBadRequestResponse(getJsonFileAsString("mock/invalid/mock_invalid_with_less_NxN_allowed_payload.json"));
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenBatchOfValidPayloads_whenPerformPost_shouldBeReturnStatusOkForSimiansAndForbiddenForHuman() throws Exception {
        final var simianPayloads = getPayloads("mock/mock_simian_payloads.json");
        simianPayloads.parallelStream()
                .forEach(payload -> {
                    try {
                        mockMvc.perform(
                                post(SIMIAN_ENDPOINT)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload.toString()))
                                .andExpect(status().isOk());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });

        Thread.sleep(1000);

        final var humanPayloads = getPayloads("mock/mock_human_payloads.json");

        humanPayloads.parallelStream()
                .forEach(payload -> {
                    try {
                        mockMvc.perform(
                                post(SIMIAN_ENDPOINT)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(payload.toString()))
                                .andExpect(status().isForbidden());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });

        Thread.sleep(1000);

        verifyDatabaseWithQuery("expected_valid_human_and_simian_payloads.xml", SPECIES_ANALYSIS_TABLE, "SELECT * FROM " + SPECIES_ANALYSIS_TABLE + " ORDER BY UUID", "ANALYZED_AT");
        verifyDatabase("expected_species_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE);

        //databaseExpectationBuilder().addExpectation(SPECIES_ANALYSIS_TABLE, ).write("expected/datasets/expected_valid_human_and_simian_payloads.xml");
        //databaseExpectationBuilder().addExpectation(SPECIES_ANALYSIS_COUNTER_TABLE).write("expected/datasets/expected_species_counter.xml");
    }

    private ArrayList<JsonNode> getPayloads(final String pathMockPayloads) throws IOException {
        final var mockPayloads = getJsonNodeFromJsonFile(pathMockPayloads);
        final var payloads = new ArrayList<JsonNode>();

        for(final var payload : mockPayloads) {
            payloads.add(payload);
        }

        return payloads;
    }

    private void sendPostAndValidatedBadRequestResponse(final String jsonPayload) throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
