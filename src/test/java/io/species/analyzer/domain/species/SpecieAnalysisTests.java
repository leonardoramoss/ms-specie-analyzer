package io.species.analyzer.domain.species;

import io.species.analyzer.domain.event.DomainEvent;
import io.species.analyzer.domain.event.DomainEventNotifier;
import io.species.analyzer.domain.event.EventNotifier;
import io.species.analyzer.domain.event.SpecieAnalyzedEvent;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import io.species.analyzer.infrastructure.exception.SpecieAnalysisNotMatchesException;
import io.species.analyzer.infrastructure.exception.SpecieValidationException;
import io.species.analyzer.infrastructure.generator.DnaSpecieUUIDGenerator;
import io.species.analyzer.infrastructure.generator.UUIDGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpecieAnalysisTests {

    final UUIDGenerator<SpecieAnalysis> generator = new DnaSpecieUUIDGenerator();
    final Analyzer primateAnalyzer = SpeciesAnalysis -> Optional.of(Specie.SIMIAN);
    final Analyzer emptyPrimateAnalyzer = SpeciesAnalysis -> Optional.empty();
    final Analyzer humanAnalyzer = SpeciesAnalysis -> Optional.of(Specie.HUMAN);

    final EventNotifier<DomainEvent> mockEventNotifier = mock(DomainEventNotifier.class);

    final static String[] SIMIAN_DNA_SEQUENCE = new String[] { "ATCGCA", "TCTCCG", "TGGTTG", "CCTTTC", "GTAATC", "ACCACT" };
    final static String[] HUMAN_DNA_SEQUENCE = new String[] { "ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG" };
    final static String[] NOT_MINIMUM_NxN_DNA_SEQUENCE = new String[] { "ATC", "GCC", "ACC" };
    final static String[] NOT_A_NxN_DNA_SEQUENCE = new String[] { "ATCA", "GCCC", "TACC", "AAGC", "TTTTA" };
    final static String[] NOT_A_VALID_DNA_SEQUENCE_U_IS_NOT_VALID = new String[] { "ATCA", "GCCC", "TACC", "AUCA" };

    final static String EXCEPTION_MESSAGE_DNA_NOT_VALID = "DNA sequence %s is invalid.";
    final static String EXCEPTION_MESSAGE_NOT_A_NxN_SEQUENCE = "There is not a NxN DNA sequence.";
    final static String EXCEPTION_MESSAGE_PART_OF_DNA_SEQUENCE_NOT_VALID = "DNA sequence %s in %s is not valid.";

    @Test
    void GivenASimianDNAAndSpecieMarkedAsSimian_shouldBeAllMatches() {
        // "c0d7dab2-e85b-3029-94f7-0cb598b4e3e0"
        final var expectedUUID = UUID.nameUUIDFromBytes(String.join("-", SIMIAN_DNA_SEQUENCE).getBytes());

        final var speciesAnalysis =
                SpecieAnalysis.of(SIMIAN_DNA_SEQUENCE).expectedSpecieAs(Specie.SIMIAN)
                        .with(primateAnalyzer, mockEventNotifier::notify).withUUID(generator);

        assertEquals(Arrays.toString(SIMIAN_DNA_SEQUENCE), Arrays.toString(speciesAnalysis.getOriginalDna()));
        assertEquals(Specie.SIMIAN, speciesAnalysis.getExpectedSpecie());
        assertEquals(Specie.SIMIAN, speciesAnalysis.getSpecie());
        assertEquals(expectedUUID.toString(), speciesAnalysis.getUuid().toString());

        verify(mockEventNotifier, times(1)).notify(any(DomainEvent.class));
    }

    @Test
    void GivenAHumanDNAAndSpecieMarkedAsHuman_shouldBeAllMatches() {
        // "053a06a4-5b45-3e69-8f1c-cadf36bd0950"
        final var expectedUUID = UUID.nameUUIDFromBytes(String.join("-", HUMAN_DNA_SEQUENCE).getBytes());

        final var speciesAnalysis =
                SpecieAnalysis.of(HUMAN_DNA_SEQUENCE).expectedSpecieAs(Specie.HUMAN)
                        .with(humanAnalyzer, mockEventNotifier::notify).withUUID(generator);

        assertEquals(Arrays.toString(HUMAN_DNA_SEQUENCE), Arrays.toString(speciesAnalysis.getOriginalDna()));
        assertEquals(Specie.HUMAN, speciesAnalysis.getExpectedSpecie());
        assertEquals(Specie.HUMAN, speciesAnalysis.getSpecie());
        assertEquals(expectedUUID.toString(), speciesAnalysis.getUuid().toString());

        verify(mockEventNotifier, times(1)).notify(any(DomainEvent.class));
    }

    @Test
    void GivenAHumanDNA_whenExpectedSpecieMarkedSimianAndHasSpecieMarkedAsHuman_shouldBeMatchesReturnFalse() {
        final var speciesAnalysis = SpecieAnalysis.of(HUMAN_DNA_SEQUENCE).expectedSpecieAs(Specie.SIMIAN);
        assertThrows(SpecieAnalysisNotMatchesException.class, () -> speciesAnalysis.with(humanAnalyzer));
    }

    @Test
    void GivenASimianDNA_whenExpectedSpecieMarkedHumanAndHasSpecieMarkedAsSimian_shouldBeMatchesReturnFalse() {
        final var speciesAnalysis = SpecieAnalysis.of(SIMIAN_DNA_SEQUENCE).expectedSpecieAs(Specie.HUMAN);
        assertThrows(SpecieAnalysisNotMatchesException.class, () -> speciesAnalysis.with(primateAnalyzer));
    }

    @Test
    void GivenAHumanDNA_whenExpectedSpecieMarkedHumanAndHasSpecieMarkedAsHuman_shouldBeMatchesReturnTrue() {
        final var speciesAnalysis = SpecieAnalysis.of(HUMAN_DNA_SEQUENCE).expectedSpecieAs(Specie.HUMAN).with(emptyPrimateAnalyzer);
        assertEquals(Specie.HUMAN, speciesAnalysis.getSpecie());
    }

    @Test
    void GivenAHumanDNA_whenExpectedSpecieMarkedHumanAndHasNoSpecieMarked_shouldBeReturnSpecieNotIdentified() {
        final var speciesAnalysis = SpecieAnalysis.of(HUMAN_DNA_SEQUENCE).expectedSpecieAs(Specie.HUMAN);
        assertEquals(Specie.NOT_IDENTIFIED, speciesAnalysis.getSpecie());
    }

    @Test
    void GivenNullDNA_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class, () -> SpecieAnalysis.of(null));
        assertEquals(String.format(EXCEPTION_MESSAGE_DNA_NOT_VALID, "null"), exception.getMessage());
    }

    @Test
    void GivenEmptyDNA_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class, () -> SpecieAnalysis.of(new String[]{}));
        assertEquals(String.format(EXCEPTION_MESSAGE_DNA_NOT_VALID, Arrays.toString(new String[]{})), exception.getMessage());
    }

    @Test
    void GivenANotMinimumNxNDNA_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class, () -> SpecieAnalysis.of(NOT_MINIMUM_NxN_DNA_SEQUENCE));
        assertEquals(String.format(EXCEPTION_MESSAGE_DNA_NOT_VALID, Arrays.toString(NOT_MINIMUM_NxN_DNA_SEQUENCE)), exception.getMessage());
    }

    @Test
    void GivenNotANxNDNA_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class, () -> SpecieAnalysis.of(NOT_A_NxN_DNA_SEQUENCE));
        assertEquals(EXCEPTION_MESSAGE_NOT_A_NxN_SEQUENCE, exception.getMessage());
    }

    @Test
    void GivenANotValidDNASequence_shouldBeThrowSpecieValidationException() {
        final var exception = assertThrows(SpecieValidationException.class, () -> SpecieAnalysis.of(NOT_A_VALID_DNA_SEQUENCE_U_IS_NOT_VALID));
        assertEquals(String.format(EXCEPTION_MESSAGE_PART_OF_DNA_SEQUENCE_NOT_VALID, "AUCA", Arrays.toString(NOT_A_VALID_DNA_SEQUENCE_U_IS_NOT_VALID)), exception.getMessage());
    }
}
