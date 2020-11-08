package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.SpecieAnalysis;
import io.species.analyzer.domain.species.analyzer.munipulation.DnaManipulation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class SimianAnalyzer implements Analyzer {

    @Override
    public Optional<Specie> analyze(final SpecieAnalysis specieAnalysis) {
        return isSimian(specieAnalysis.getOriginalDna())
                ? Optional.of(Specie.SIMIAN)
                : Optional.empty();
    }

    /**
     *
     * @param dna
     * @return
     */
     boolean isSimian(final String[] dna) {

        final var pattern = Pattern.compile(".*(A{4}|C{4}|G{4}|T{4}).*");

         return isSimian(dna, pattern) ||
                 isSimian(DnaManipulation.transposeReversedDiagonalDNASequence(dna), pattern) ||
                 isSimian(DnaManipulation.transposeDNASequence(dna), pattern) ||
                 isSimian(DnaManipulation.transposeDiagonalDNASequence(dna), pattern);
    }

    private boolean isSimian(final String[] dna, final Pattern pattern) {
        return Arrays.stream(dna).anyMatch(it -> pattern.matcher(it).matches());
    }
}
