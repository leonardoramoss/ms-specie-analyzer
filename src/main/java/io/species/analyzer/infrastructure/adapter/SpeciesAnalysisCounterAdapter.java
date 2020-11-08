package io.species.analyzer.infrastructure.adapter;

import io.species.analyzer.domain.species.SpecieAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisCounter;
import org.springframework.stereotype.Component;

@Component
public class SpeciesAnalysisCounterAdapter implements Adapter<SpecieAnalysis, SpeciesAnalysisCounter> {

    @Override
    public SpeciesAnalysisCounter adapt(final SpecieAnalysis argument) {
        return SpeciesAnalysisCounter.of(argument.getSpecie());
    }
}
