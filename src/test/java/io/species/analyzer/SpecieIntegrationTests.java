package io.species.analyzer;

import io.species.analyzer.infrastructure.AbstractIntegrationTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpecieIntegrationTests extends AbstractIntegrationTests {

    private static final String SIMIAN_ENDPOINT = "/simian";
    private static final String DNA_TABLE = "SPECIE.DNA";

    @Test
    @SqlGroup({
        @Sql(scripts = { "classpath:clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @DisplayName(value = "")
    void GivenAValidSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk() throws Exception {
        mockMvc.perform(
                    post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_specie.xml", DNA_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @DisplayName(value = "")
    void GivenAValidSimianDna_whenPerformPostAnAlreadySavedDna_shouldBeReturnFromDatabaseAndStatusOk() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_specie.xml", DNA_TABLE);

        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_specie.xml", DNA_TABLE);
    }

    @Test
    @SqlGroup({
        @Sql(scripts = { "classpath:clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHumanDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusForbidden() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_human_payload.json")))
                .andExpect(status().isForbidden());

        verifyDatabase("expected_human_specie.xml", DNA_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidDna_whenPerformPost_shouldBeReturnStatusBadRequest() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_invalid_payload.json")))
                .andExpect(status().isBadRequest());

        verifyDatabase("expected_invalid_specie.xml", DNA_TABLE);
    }
}
