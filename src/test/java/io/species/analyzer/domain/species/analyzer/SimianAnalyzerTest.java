package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.SpecieAnalysis;
import io.species.analyzer.infrastructure.exception.SpecieAnalysisNotMatchesException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimianAnalyzerTest {

    final SimianAnalyzer analyzer = new SimianAnalyzer();

    static final String[] HORIZONTAL_SIMIAN = { "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG" };
    static final String[] VERTICAL_SIMIAN = { "CTTAGA", "CTATGC", "CGTCTT", "ACACGT", "CCTCTA", "TCACTG" };
    static final String[] DIAGONAL_SIMIAN = { "CTTAGA", "CTATGC", "CATCTT", "ACACGT", "CCTGTA", "TCACTG" };
    static final String[] REVERSED_DIAGONAL_SIMIAN = { "CTTAGA", "CTATGC", "CTTCTT", "ACACGT", "CCTGTA", "TCACTG" };

    static final String[] HUMAN = { "ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG" };

    @Test
    void GivenHorizontalSimianDNASequenceAndExpectedAsSimian_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpecieAnalysis.of(HORIZONTAL_SIMIAN).expectedSpecieAs(Specie.SIMIAN);
        assertEquals(speciesAnalysis.with(analyzer).getSpecie(), Specie.SIMIAN);
    }

    @Test
    void GivenVerticalSimianDNASequenceAndExpectedAsSimian_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpecieAnalysis.of(VERTICAL_SIMIAN).expectedSpecieAs(Specie.SIMIAN);
        assertEquals(speciesAnalysis.with(analyzer).getSpecie(), Specie.SIMIAN);
    }

    @Test
    void GivenDiagonalSimianDNASequenceAndExpectedAsSimian_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpecieAnalysis.of(DIAGONAL_SIMIAN).expectedSpecieAs(Specie.SIMIAN);
        assertEquals(speciesAnalysis.with(analyzer).getSpecie(), Specie.SIMIAN);
    }

    @Test
    void GivenReversedDiagonalSimianDNASequenceAndExpectedAsSimian_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpecieAnalysis.of(REVERSED_DIAGONAL_SIMIAN).expectedSpecieAs(Specie.SIMIAN);
        assertEquals(speciesAnalysis.with(analyzer).getSpecie(), Specie.SIMIAN);
    }

    @Test
    void GivenHumanDNASequenceAndExpectedAsHuman_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpecieAnalysis.of(HUMAN).expectedSpecieAs(Specie.HUMAN);
        assertEquals(Specie.HUMAN, speciesAnalysis.with(analyzer).getSpecie());
    }

    @Test
    void GivenHumanDNASequenceAndExpectedAsSimian_shouldBeThrowsExceptionNotMatches() {
        final var speciesAnalysis = SpecieAnalysis.of(HUMAN).expectedSpecieAs(Specie.SIMIAN);
        assertThrows(SpecieAnalysisNotMatchesException.class, () -> speciesAnalysis.with(analyzer));
    }
}
