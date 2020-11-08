package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.SpecieAnalysis;

import java.util.Optional;

@FunctionalInterface
public interface Analyzer {

    /**
     *
     * @param specieAnalysis
     * @return
     */
    Optional<Specie> analyze(final SpecieAnalysis specieAnalysis);
}
