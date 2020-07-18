package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.Species;
import io.species.analyzer.infrastructure.util.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import static io.species.analyzer.infrastructure.util.ArrayUtils.transposeStringArrayValues;

@Component
public class PrimateAnalyzer implements Analyzer {

    private static final Map<String, Boolean> responseMap = Map.of("ATGCGA-CAGTGC-TTATGT-AGAAGG-CCCCTA-TCACTG", Boolean.TRUE, "ATGCGA-CAGTGC-TTATTT-AGACGG-GCGTCA-TCACTG", Boolean.FALSE);


    @Override
    public Specie analyze(final Specie specie) {
        final boolean isSimian = isSimian(specie.getOriginalDna());

        if(isSimian) {
            specie.markAs(Species.SIMIAN);
        } else {
            specie.markAs(Species.HUMAN);
        }

        return specie;
    }

    /**
     *
     * @param dna
     * @return
     */
     boolean isSimian(String[] dna) {
        final var pattern = Pattern.compile(".*(A{4}|C{4}|G{4}|T{4}).*");

        final boolean hasHorizontalSequenceMatched = isSimian(dna, pattern);

        if(hasHorizontalSequenceMatched) {
            return true;
        }

        final boolean hasVerticalSequenceMatched = isSimian(transposeStringArrayValues(dna), pattern);

        if(hasVerticalSequenceMatched) {
            return true;
        }

         final char[][] arrayToMultidimensionalChar = ArrayUtils.stringArrayToMultidimensionalChar(dna);
         final String[] strings = ArrayUtils.diagonalMultidimensionalCharToArrayString(arrayToMultidimensionalChar);
         final boolean hasDiagonalSequenceMatched = isSimian(strings, pattern);
         
         if(hasDiagonalSequenceMatched) {
             return true;
         }

         final String[] reverseStringArrayValues = ArrayUtils.reverseStringArrayValues(dna);
         final char[][] multidimensionalChar = ArrayUtils.stringArrayToMultidimensionalChar(reverseStringArrayValues);
         final String[] strings1 = ArrayUtils.diagonalMultidimensionalCharToArrayString(multidimensionalChar);

         final boolean hasReversedDiagonalSequenceMatched = isSimian(strings1, pattern);

         return hasReversedDiagonalSequenceMatched;
    }

     boolean isSimian(final String[] dna, final Pattern pattern) {
        return Arrays.stream(dna).anyMatch(dnaSequence -> pattern.matcher(dnaSequence).matches());
    }
}
