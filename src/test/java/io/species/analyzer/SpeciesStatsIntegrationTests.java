package io.species.analyzer;

import io.species.analyzer.application.SpeciesApplicationServices;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import io.species.analyzer.infrastructure.exception.StatsExecutorException;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static io.species.analyzer.configuration.fixtures.JsonFixture.loadJsonFile;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
class SpeciesStatsIntegrationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private SpeciesApplicationServices speciesApplicationServices;

    private static final String STATS_ENDPOINT = "/v1/stats";

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenPreviousSpeciesAnalyzed_whenPerformGetStats_shouldBeReturnRatioStatsAndStatusOk() throws Exception {
        final var mvcResult = performGetAndExpect(status().isOk());
        assertEquals(loadJsonFile("expected/response/stats/expected_stats.json"), mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenNoneSpeciesAnalyzed_whenPerformGetStats_shouldBeReturnRatioZeroStatsAndStatusOk() throws Exception {
        final var mvcResult = performGetAndExpect(status().isOk());
        assertEquals(loadJsonFile("expected/response/stats/expected_zero_stats.json"), mvcResult.getResponse().getContentAsString(), true);
    }

    @Test
    void GivenANotIdentifierStats_whenPerformViewStats_shouldBeThrowsStatsExecutorException() {
        assertThrows(StatsExecutorException.class, () -> speciesApplicationServices.viewStats(StatsIdentifier.NOT_IDENTIFIED));
    }

    protected MvcResult performGetAndExpect(final ResultMatcher expected) {
        try {
            return mockMvc.perform(get(SpeciesStatsIntegrationTests.STATS_ENDPOINT)).andExpect(expected).andReturn();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
