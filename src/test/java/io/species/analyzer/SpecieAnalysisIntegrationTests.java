package io.species.analyzer;

import io.species.analyzer.application.SpeciesApplicationServices;
import io.species.analyzer.configuration.fixtures.DatabaseAssertion;
import io.species.analyzer.configuration.fixtures.DatabaseFixture;
import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.infrastructure.exception.SpecieAnalyzerException;
import io.species.analyzer.infrastructure.serialization.deserializer.SpeciesAnalysisWrapperDeserializer;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import org.springframework.test.web.servlet.ResultMatcher;

import static io.species.analyzer.configuration.fixtures.JsonFixture.loadJsonFile;
import static io.species.analyzer.configuration.fixtures.JsonFixture.loadJsonFileAsJsonNode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
class SpecieAnalysisIntegrationTests implements DatabaseAssertion {

    @Autowired
    private DatabaseFixture databaseFixture;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private SpeciesApplicationServices speciesApplicationServices;

    @Autowired
    private SpeciesAnalysisWrapperDeserializer speciesAnalysisWrapperDeserializer;

    private static final String SIMIAN_ENDPOINT = "/v1/analyze/simian";
    private static final String UNKNOWN_SPECIE_ENDPOINT = "/v1/analyze/unknown";

    private static final String SPECIES_ANALYSIS_TABLE = "SPECIE.SPECIES_ANALYSIS";
    private static final String SPECIES_ANALYSIS_COUNTER_TABLE = "SPECIE.SPECIES_ANALYSIS_COUNTER";

    @Override
    public void verifyDatabase(String fileName, String tableName, String query, String ... ignoredColumns) {
        databaseFixture.verifyDatabase(fileName, tableName, query, ignoredColumns);
    }

    @SqlGroup({
        @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @CsvSource({
            "mock/mock_simian_horizontal_payload.json,expected_simian_horizontal_specie.xml,expected_simian_counter.xml",
            "mock/mock_simian_vertical_payload.json,expected_simian_vertical_species.xml,expected_simian_counter.xml",
            "mock/mock_simian_diagonal_payload.json,expected_simian_diagonal_species.xml,expected_simian_counter.xml",
            "mock/mock_simian_reversed_diagonal_payload.json,expected_simian_reversed_diagonal_species.xml,expected_simian_counter.xml",
    })
    @ParameterizedTest
    void GivenAValidSimianDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusOk(String payload, String tableSpecieAnalysis, String tableSpecieAnalysisCounter) {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, loadJsonFile(payload), status().isOk());
        verifyDatabase(tableSpecieAnalysis, SPECIES_ANALYSIS_TABLE, null, "ANALYZED_AT");
        verifyDatabase(tableSpecieAnalysisCounter, SPECIES_ANALYSIS_COUNTER_TABLE, null);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHorizontalSimianDna_whenPerformPostAnAlreadySavedDna_shouldBeReturnFromDatabaseAndStatusOk() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, loadJsonFile("mock/mock_simian_horizontal_payload.json"), status().isOk());

        verifyDatabase("expected_simian_horizontal_specie.xml", SPECIES_ANALYSIS_TABLE, null, "ANALYZED_AT");

        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, loadJsonFile("mock/mock_simian_horizontal_payload.json"), status().isOk());

        verifyDatabase("expected_simian_horizontal_specie.xml", SPECIES_ANALYSIS_TABLE, null, "ANALYZED_AT");
        verifyDatabase("expected_simian_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE, null);
    }

    @Test
    @SqlGroup({
        @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidHumanDna_whenPerformPost_shouldBeProcessedAndSavedOnDatabaseAndReturnStatusForbidden() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, loadJsonFile("mock/mock_human_payload.json"), status().isForbidden());
        verifyDatabase("expected_human_specie.xml", SPECIES_ANALYSIS_TABLE, null, "ANALYZED_AT");
        verifyDatabase("expected_human_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE, null);
    }

    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @CsvSource({
            "mock/invalid/mock_invalid_not_allowed_character_payload.json,expected_invalid_specie.xml,expected_empty_counter.xml",
            "mock/invalid/mock_invalid_empty_dna_payload.json,expected_invalid_specie.xml,expected_empty_counter.xml",
            "mock/invalid/mock_invalid_without_dna_label_payload.json,expected_invalid_specie.xml,expected_empty_counter.xml",
            "mock/invalid/mock_invalid_not_a_NxN_payload.json,expected_invalid_specie.xml,expected_empty_counter.xml",
            "mock/invalid/mock_invalid_with_less_NxN_allowed_payload.json,expected_invalid_specie.xml,expected_empty_counter.xml"
    })
    @ParameterizedTest
    void GivenAInvalidPayload_whenPerformPost_shouldBeReturnStatusBadRequest(String payload, String tableSpecieAnalysis, String tableSpecieAnalysisCounter) {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, loadJsonFile(payload), status().isBadRequest());
        verifyDatabase(tableSpecieAnalysis, SPECIES_ANALYSIS_TABLE, null);
        verifyDatabase(tableSpecieAnalysisCounter, SPECIES_ANALYSIS_COUNTER_TABLE, null);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAInvalidPayloadWithNullDNA_whenPerformPost_shouldBeReturnStatusBadRequest() {
        performPostWithPayloadAndExpect(SIMIAN_ENDPOINT, "{ \"dna\": }", status().isBadRequest());
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE, null);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE, null);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenANotAnalyzerConfigured_whenPerformAnalyzeSpecie_shouldBeThrowsSpecieAnalyzerException() {
        final var speciesAnalysisWrapper = speciesAnalysisWrapperDeserializer.deserialize(loadJsonFileAsJsonNode("mock/mock_simian_horizontal_payload.json"));
        speciesAnalysisWrapper.getSpecie().ifPresent(it -> {

            assertThrows(SpecieAnalyzerException.class, () -> speciesApplicationServices.analyzeSpecie(it.expectedSpecieAs(Specie.HUMAN)));

            verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE, null);
            verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE, null);
        });
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = { "classpath:scripts/clear.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void GivenAValidPayload_whenPerformAnalyzeSpecieForUnknownSpecie_shouldBeThrowsSpecieNotFoundException() {
        performPostWithPayloadAndExpect(UNKNOWN_SPECIE_ENDPOINT, loadJsonFile("mock/mock_simian_horizontal_payload.json"), status().isBadRequest());
        verifyDatabase("expected_invalid_specie.xml", SPECIES_ANALYSIS_TABLE, null);
        verifyDatabase("expected_empty_counter.xml", SPECIES_ANALYSIS_COUNTER_TABLE, null);
    }

    protected void performPostWithPayloadAndExpect(final String endpoint, final String jsonPayload, final ResultMatcher expected) {
        try {
            mockMvc.perform(
                    post(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    .andExpect(expected)
                    .andReturn();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
