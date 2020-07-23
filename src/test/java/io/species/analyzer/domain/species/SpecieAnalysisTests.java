package io.species.analyzer.domain.species;

import io.species.analyzer.infrastructure.AbstractTests;
import io.species.analyzer.infrastructure.exception.SpecieValidationException;
import io.species.analyzer.infrastructure.generator.DnaSpecieUUIDGenerator;
import io.species.analyzer.infrastructure.generator.UUIDGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SpecieAnalysisTests extends AbstractTests {

    final UUIDGenerator<SpeciesAnalysis> generator = new DnaSpecieUUIDGenerator();

    final static String[] SIMIAN_DNA_SEQUENCE = new String[] { "ATCGCA", "TCTCCG", "TGGTTG", "CCTTTC", "GTAATC", "ACCACT" };
    final static String[] HUMAN_DNA_SEQUENCE = new String[] { "ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG" };
    final static String[] NOT_MINIMUM_NxN_DNA_SEQUENCE = new String[] { "ATC", "GCC", "ACC" };
    final static String[] NOT_A_NxN_DNA_SEQUENCE = new String[] { "ATCA", "GCCC", "TACC", "AAGC", "TTTTA" };
    final static String[] NOT_A_VALID_DNA_SEQUENCE_U_IS_NOT_VALID = new String[] { "ATCA", "GCCC", "TACC", "AUCA" };

    final static String EXCEPTION_MESSAGE_DNA_NOT_VALID = "DNA sequence %s is invalid.";
    final static String EXCEPTION_MESSAGE_NOT_A_NxN_SEQUENCE = "There is not a NxN DNA sequence.";
    final static String EXCEPTION_MESSAGE_PART_OF_DNA_SEQUENCE_NOT_VALID = "DNA sequence %s in %s is not valid.";

    @Test
    void GivenASimianDNAAndIdentifierMarkedAsSimian_shouldBeAllMatches() {
        // "c0d7dab2-e85b-3029-94f7-0cb598b4e3e0"
        final var expectedUUID = UUID.nameUUIDFromBytes(String.join("-", SIMIAN_DNA_SEQUENCE).getBytes());

        final var speciesAnalysis =
                SpeciesAnalysis.of(SIMIAN_DNA_SEQUENCE)
                        .markIdentifiedAs(SpeciesIdentifier.SIMIAN)
                        .withUUID(generator);

        assertEquals(Arrays.toString(SIMIAN_DNA_SEQUENCE), Arrays.toString(speciesAnalysis.getOriginalDna()));
        assertEquals(SpeciesIdentifier.SIMIAN, speciesAnalysis.getExpectedIdentifier());
        assertEquals(SpeciesIdentifier.SIMIAN, speciesAnalysis.getIdentifier());
        assertEquals(expectedUUID.toString(), speciesAnalysis.getUuid().toString());
    }

    @Test
    void GivenAHumanDNAAndIdentifierMarkedAsHuman_shouldBeAllMatches() {
        // "053a06a4-5b45-3e69-8f1c-cadf36bd0950"
        final var expectedUUID = UUID.nameUUIDFromBytes(String.join("-", HUMAN_DNA_SEQUENCE).getBytes());

        final var speciesAnalysis =
                SpeciesAnalysis.of(HUMAN_DNA_SEQUENCE)
                        .markIdentifiedAs(SpeciesIdentifier.HUMAN)
                        .withUUID(generator);

        assertEquals(Arrays.toString(HUMAN_DNA_SEQUENCE), Arrays.toString(speciesAnalysis.getOriginalDna()));
        assertEquals(SpeciesIdentifier.HUMAN, speciesAnalysis.getExpectedIdentifier());
        assertEquals(SpeciesIdentifier.HUMAN, speciesAnalysis.getIdentifier());
        assertEquals(expectedUUID.toString(), speciesAnalysis.getUuid().toString());
    }

    @Test
    void GivenAHumanDNA_whenExpectedIdentifierMarkedSimianAndHasIdentifierMarkedAsHuman_shouldBeMatchesReturnFalse() {
        final var speciesAnalysis =
                SpeciesAnalysis.of(HUMAN_DNA_SEQUENCE)
                        .markExpectedIdentifierAs(SpeciesIdentifier.SIMIAN)
                        .markIdentifiedAs(SpeciesIdentifier.HUMAN);

        assertFalse(speciesAnalysis.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenASimianDNA_whenExpectedIdentifierMarkedHumanAndHasIdentifierMarkedAsSimian_shouldBeMatchesReturnFalse() {
        final var speciesAnalysis =
                SpeciesAnalysis.of(SIMIAN_DNA_SEQUENCE)
                        .markExpectedIdentifierAs(SpeciesIdentifier.HUMAN)
                        .markIdentifiedAs(SpeciesIdentifier.SIMIAN);

        assertFalse(speciesAnalysis.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenAHumanDNA_whenExpectedIdentifierMarkedHumanAndHasIdentifierMarkedAsHuman_shouldBeMatchesReturnTrue() {
        final var speciesAnalysis =
                SpeciesAnalysis.of(HUMAN_DNA_SEQUENCE)
                        .markExpectedIdentifierAs(SpeciesIdentifier.HUMAN)
                        .markIdentifiedAs(SpeciesIdentifier.HUMAN);

        assertTrue(speciesAnalysis.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenAHumanDNA_whenExpectedIdentifierMarkedHumanAndHasNoIdentifierMarked_shouldBeMatchesReturnFalse() {
        final var speciesAnalysis =
                SpeciesAnalysis.of(HUMAN_DNA_SEQUENCE)
                        .markExpectedIdentifierAs(SpeciesIdentifier.HUMAN);

        assertFalse(speciesAnalysis.isIdentifierMatchesAsExpected());
    }

    @Test
    void GivenNullDNA_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class,
                () -> SpeciesAnalysis.of(null));
        assertEquals(String.format(EXCEPTION_MESSAGE_DNA_NOT_VALID, null), exception.getMessage());
    }

    @Test
    void GivenEmptyDNA_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class,
                () -> SpeciesAnalysis.of(new String[]{}));
        assertEquals(String.format(EXCEPTION_MESSAGE_DNA_NOT_VALID, Arrays.toString(new String[]{})), exception.getMessage());
    }

    @Test
    void GivenANotMinimumNxNDNA_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class,
                () -> SpeciesAnalysis.of(NOT_MINIMUM_NxN_DNA_SEQUENCE));
        assertEquals(String.format(EXCEPTION_MESSAGE_DNA_NOT_VALID, Arrays.toString(NOT_MINIMUM_NxN_DNA_SEQUENCE)), exception.getMessage());
    }

    @Test
    void GivenNotANxNDNA_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class,
                () -> SpeciesAnalysis.of(NOT_A_NxN_DNA_SEQUENCE));
        assertEquals(EXCEPTION_MESSAGE_NOT_A_NxN_SEQUENCE, exception.getMessage());
    }

    @Test
    void GivenANotValidDNASequence_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class,
                () -> SpeciesAnalysis.of(NOT_A_VALID_DNA_SEQUENCE_U_IS_NOT_VALID));
        assertEquals(String.format(EXCEPTION_MESSAGE_PART_OF_DNA_SEQUENCE_NOT_VALID, "AUCA", Arrays.toString(NOT_A_VALID_DNA_SEQUENCE_U_IS_NOT_VALID)), exception.getMessage());
    }
}
