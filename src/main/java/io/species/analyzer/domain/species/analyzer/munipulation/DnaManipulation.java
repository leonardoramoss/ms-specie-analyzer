package io.species.analyzer.domain.species.analyzer.munipulation;

import static io.species.analyzer.infrastructure.util.ArrayUtils.diagonalMultidimensionalCharToArrayString;
import static io.species.analyzer.infrastructure.util.ArrayUtils.explodeValues;
import static io.species.analyzer.infrastructure.util.ArrayUtils.multidimensionalCharToStringArray;
import static io.species.analyzer.infrastructure.util.ArrayUtils.reverseStringArrayValues;
import static io.species.analyzer.infrastructure.util.ArrayUtils.transposeRowToColumn;

public class DnaManipulation {

    private DnaManipulation() {}

    /**
     * Given an array of strings this method will transpose from row to column values
     *
     * Original    Transposed
     * A A A A     A B C D
     * B B B B     A B C D
     * C C C C     A B D D
     * D D D D     A B C D
     *
     * @param stringArray
     * @return transposed array
     */
    public static String[] transposeDNASequence(final String[] stringArray) {
        final var arrayToMultidimensionalChar = explodeValues(stringArray);
        final var transposeRowToColumn = transposeRowToColumn(arrayToMultidimensionalChar);
        return multidimensionalCharToStringArray(transposeRowToColumn);
    }

    /**
     * Given an array of strings this method will transpose diagonal from left to right for row values
     *
     * Original    Transposed
     * A B C D         A
     * B C D A        B B
     * C D A B       C C C
     * D A B C      D D D D
     *               A A A
     *                B B
     *                 C
     *
     * @param dna
     * @return transposed array
     */
    public static String[] transposeDiagonalDNASequence(final String[] dna) {
        return diagonalMultidimensionalCharToArrayString(explodeValues(dna));
    }

    /**
     * Given an array of strings this method will transpose diagonal from right to left for row values
     *
     * Original    Transposed
     * A B C D         D
     * B C D A        A C
     * C D A B       B D B
     * D A B C      C A C A
     *               B D B
     *                A C
     *                 D
     *
     * @param dna
     * @return transposed array
     */
    public static String[] transposeReversedDiagonalDNASequence(final String[] dna) {
        return transposeDiagonalDNASequence(reverseStringArrayValues(dna));
    }
}
