package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.SpeciesAnalysis;

@FunctionalInterface
public interface Analyzer {

    /**
     *
     * @param speciesAnalysis
     * @return
     */
    SpeciesAnalysis analyze(final SpeciesAnalysis speciesAnalysis);
}
