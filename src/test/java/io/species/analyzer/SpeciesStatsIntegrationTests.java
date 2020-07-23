package io.species.analyzer;

import io.species.analyzer.application.SpeciesApplicationServices;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import io.species.analyzer.infrastructure.AbstractIntegrationTests;
import io.species.analyzer.infrastructure.exception.StatsExecutorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpeciesStatsIntegrationTests extends AbstractIntegrationTests {

    private static final String STATS_ENDPOINT = "/v1/stats";

    @Autowired
    private SpeciesApplicationServices speciesApplicationServices;

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenPreviousSpeciesAnalyzed_whenPerformGetStats_shouldBeReturnRatioStatsAndStatusOk() throws Exception {
        final var mvcResult = performGetAndExpect(STATS_ENDPOINT, status().isOk());
        assertEquals(getJsonFileAsString("expected/response/stats/expected_stats.json"), getMvcResultAsString(mvcResult), true);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenNoneSpeciesAnalyzed_whenPerformGetStats_shouldBeReturnRatioZeroStatsAndStatusOk() throws Exception {
        final var mvcResult = performGetAndExpect(STATS_ENDPOINT, status().isOk());
        assertEquals(getJsonFileAsString("expected/response/stats/expected_zero_stats.json"), getMvcResultAsString(mvcResult), true);
    }

    @Test
    void GivenANotIdentifierStats_whenPerformViewStats_shouldBeThrowsStatsExecutorException() {
        assertThrows(StatsExecutorException.class, () -> speciesApplicationServices.viewStats(StatsIdentifier.NOT_IDENTIFIED));
    }
}
