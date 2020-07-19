package io.species.analyzer;

import io.species.analyzer.infrastructure.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MvcResult;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpeciesAnalysisIntegrationTests extends AbstractIntegrationTests {

    private static final String SIMIAN_ENDPOINT = "/v1/simian";
    private static final String STATS_ENDPOINT = "/v1/stats";

    private static final String DNA_TABLE = "SPECIE.SPECIES_ANALYSIS";

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

        verifyDatabase("expected_simian_horizontal_specie.xml", DNA_TABLE, "ANALYZED_AT");
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

        verifyDatabase("expected_simian_vertical_species.xml", DNA_TABLE, "ANALYZED_AT");
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

        verifyDatabase("expected_simian_diagonal_species.xml", DNA_TABLE, "ANALYZED_AT");
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

        verifyDatabase("expected_simian_reversed_diagonal_species.xml", DNA_TABLE, "ANALYZED_AT");
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

        verifyDatabase("expected_simian_horizontal_specie.xml", DNA_TABLE, "ANALYZED_AT");

        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_simian_horizontal_payload.json")))
                .andExpect(status().isOk());

        verifyDatabase("expected_simian_horizontal_specie.xml", DNA_TABLE, "ANALYZED_AT");
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

        verifyDatabase("expected_human_specie.xml", DNA_TABLE, "ANALYZED_AT");
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidDna_whenPerformPost_shouldBeReturnStatusBadRequest() throws Exception {
        mockMvc.perform(
                post(SIMIAN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonFileAsString("mock/mock_invalid_payload.json")))
                .andExpect(status().isBadRequest());

        verifyDatabase("expected_invalid_specie.xml", DNA_TABLE);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void test() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(STATS_ENDPOINT)).andExpect(status().isOk()).andReturn();
        assertEquals(getJsonFileAsString("expected/response/stats/expected_stats.json"), getMvcResultAsString(mvcResult), true);
    }
}
