package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimateAnalyzerTest {

    final PrimateAnalyzer analyzer = new PrimateAnalyzer();

    static final String[] HORIZONTAL_SIMIAN = { "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG" };
    static final String[] VERTICAL_SIMIAN = { "CTTAGA", "CTATGC", "CGTCTT", "ACACGT", "CCTCTA", "TCACTG" };
    static final String[] DIAGONAL_SIMIAN = { "CTTAGA", "CTATGC", "CATCTT", "ACACGT", "CCTGTA", "TCACTG" };
    static final String[] REVERSED_DIAGONAL_SIMIAN = { "CTTAGA", "CTATGC", "CTTCTT", "ACACGT", "CCTGTA", "TCACTG" };

    static final String[] HUMAN = { "ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG" };

    @Test
    void GivenHorizontalSimianDNASequenceAndExpectedAsSimian_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpeciesAnalysis.of(HORIZONTAL_SIMIAN).markExpectedIdentifierAs(SpeciesIdentifier.SIMIAN);
        final var speciesAnalyzed = analyzer.analyze(speciesAnalysis);

        assertEquals(SpeciesIdentifier.SIMIAN, speciesAnalyzed.getIdentifier());
        assertTrue(speciesAnalyzed.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenVerticallSimianDNASequenceAndExpectedAsSimian_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpeciesAnalysis.of(VERTICAL_SIMIAN).markExpectedIdentifierAs(SpeciesIdentifier.SIMIAN);
        final var speciesAnalyzed = analyzer.analyze(speciesAnalysis);

        assertEquals(SpeciesIdentifier.SIMIAN, speciesAnalyzed.getIdentifier());
        assertTrue(speciesAnalyzed.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenDiagonalSimianDNASequenceAndExpectedAsSimian_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpeciesAnalysis.of(DIAGONAL_SIMIAN).markExpectedIdentifierAs(SpeciesIdentifier.SIMIAN);
        final var speciesAnalyzed = analyzer.analyze(speciesAnalysis);

        assertEquals(SpeciesIdentifier.SIMIAN, speciesAnalyzed.getIdentifier());
        assertTrue(speciesAnalyzed.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenReversedDiagonalSimianDNASequenceAndExpectedAsSimian_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpeciesAnalysis.of(REVERSED_DIAGONAL_SIMIAN).markExpectedIdentifierAs(SpeciesIdentifier.SIMIAN);
        final var speciesAnalyzed = analyzer.analyze(speciesAnalysis);

        assertEquals(SpeciesIdentifier.SIMIAN, speciesAnalyzed.getIdentifier());
        assertTrue(speciesAnalyzed.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenHumanDNASequenceAndExpectedAsHuman_shouldBeMatchesAsExpected() {
        final var speciesAnalysis = SpeciesAnalysis.of(HUMAN).markExpectedIdentifierAs(SpeciesIdentifier.HUMAN);
        final var speciesAnalyzed = analyzer.analyze(speciesAnalysis);

        assertEquals(SpeciesIdentifier.HUMAN, speciesAnalyzed.getIdentifier());
        assertTrue(speciesAnalyzed.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenHumanDNASequenceAndExpectedAsSimian_shouldBeNotMatches() {
        final var speciesAnalysis = SpeciesAnalysis.of(HUMAN).markExpectedIdentifierAs(SpeciesIdentifier.SIMIAN);
        final var speciesAnalyzed = analyzer.analyze(speciesAnalysis);

        assertEquals(SpeciesIdentifier.HUMAN, speciesAnalyzed.getIdentifier());
        assertFalse(speciesAnalyzed.isIdentifierMatchesAsExpected());
    }
}
