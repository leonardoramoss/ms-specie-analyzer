package io.species.analyzer.domain.species.analyzer.manipulation;

import io.species.analyzer.domain.species.analyzer.munipulation.DnaManipulation;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DnaManipulationTests {

    private final static String[] DNA_SEQUENCE = { "AAAA", "BBBB", "CCCC", "DDDD" };
    private final static String[] EXPECTED_TRANSPOSED_DNA_SEQUENCE = { "ABCD", "ABCD", "ABCD", "ABCD" };

    private final static String[] DIAGONAL_DNA_SEQUENCE = { "ABCD", "BCDA", "CDAB", "DABC" };
    private final static String[] EXPECTED_DIAGONAL_DNA_SEQUENCE = { "A", "BB", "CCC", "DDDD", "AAA", "BB", "C" };
    private final static String[] EXPECTED_REVERSED_DIAGONAL_DNA_SEQUENCE = { "D", "AC", "BDB", "CACA", "BDB", "AC", "D" };

    @Test
    void transposeDNASequence() {
        final var transposeDNASequence = DnaManipulation.transposeDNASequence(DNA_SEQUENCE);
        assertEquals(Arrays.toString(EXPECTED_TRANSPOSED_DNA_SEQUENCE), Arrays.toString(transposeDNASequence));
    }

    @Test
    void transposeDiagonalDNASequence() {
        final var transposeDiagonalDNASequence = DnaManipulation.transposeDiagonalDNASequence(DIAGONAL_DNA_SEQUENCE);
        assertEquals(Arrays.toString(EXPECTED_DIAGONAL_DNA_SEQUENCE), Arrays.toString(transposeDiagonalDNASequence));
    }

    @Test
    void transposeReversedDiagonalDNASequence() {
        final var transposeDiagonalDNASequence = DnaManipulation.transposeReversedDiagonalDNASequence(DIAGONAL_DNA_SEQUENCE);
        assertEquals(Arrays.toString(EXPECTED_REVERSED_DIAGONAL_DNA_SEQUENCE), Arrays.toString(transposeDiagonalDNASequence));
    }
}
