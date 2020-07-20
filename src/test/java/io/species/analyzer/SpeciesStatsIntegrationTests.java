package io.species.analyzer;

import io.species.analyzer.infrastructure.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class SpeciesStatsIntegrationTests extends AbstractIntegrationTests {

    private static final String STATS_ENDPOINT = "/v1/stats";

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenPreviousSpeciesAnalyzed_whenPerformGetStats_shouldBeReturnRatioStatsAndStatusOk() throws Exception {
        final var mvcResult = mockMvc.perform(get(STATS_ENDPOINT)).andExpect(status().isOk()).andReturn();
        assertEquals(getJsonFileAsString("expected/response/stats/expected_stats.json"), getMvcResultAsString(mvcResult), true);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:sqls/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenNoneSpeciesAnalyzed_whenPerformGetStats_shouldBeReturnRatioZeroStatsAndStatusOk() throws Exception {
        final var mvcResult = mockMvc.perform(get(STATS_ENDPOINT)).andExpect(status().isOk()).andReturn();
        assertEquals(getJsonFileAsString("expected/response/stats/expected_zero_stats.json"), getMvcResultAsString(mvcResult), true);
    }
}
