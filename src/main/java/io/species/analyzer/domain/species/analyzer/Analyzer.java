package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.Specie;

@FunctionalInterface
public interface Analyzer {

    /**
     *
     * @param specie
     * @return
     */
    Specie analyze(final Specie specie);
}
