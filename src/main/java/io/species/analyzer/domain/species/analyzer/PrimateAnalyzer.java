package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import io.species.analyzer.domain.species.analyzer.munipulation.DnaManipulation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Pattern;

@Component
public class PrimateAnalyzer implements Analyzer {

    @Override
    public SpeciesAnalysis analyze(final SpeciesAnalysis speciesAnalysis) {
        final var isSimian = isSimian(speciesAnalysis.getOriginalDna());

        if(isSimian) {
            speciesAnalysis.markIdentifiedAs(SpeciesIdentifier.SIMIAN);
        } else {
            speciesAnalysis.markIdentifiedAs(SpeciesIdentifier.HUMAN);
        }

        return speciesAnalysis;
    }

    /**
     *
     * @param dna
     * @return
     */
     boolean isSimian(String[] dna) {

        final var pattern = Pattern.compile(".*(A{4}|C{4}|G{4}|T{4}).*");

        final var hasHorizontalSequenceMatched = isSimian(dna, pattern);
        if(hasHorizontalSequenceMatched) {
            return true;
        }

        final var hasVerticalSequenceMatched = isSimian(DnaManipulation.transposeDNASequence(dna), pattern);
        if(hasVerticalSequenceMatched) {
            return true;
        }

         final var hasDiagonalSequenceMatched = isSimian(DnaManipulation.transposeDiagonalDNASequence(dna), pattern);
         if(hasDiagonalSequenceMatched) {
             return true;
         }

         return isSimian(DnaManipulation.transposeReversedDiagonalDNASequence(dna), pattern);
    }

    private boolean isSimian(final String[] dna, final Pattern pattern) {
        return Arrays.stream(dna).anyMatch(dnaSequence -> pattern.matcher(dnaSequence).matches());
    }
}
