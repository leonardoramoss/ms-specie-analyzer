package io.species.analyzer.infrastructure.adapter;

import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisCounter;
import org.springframework.stereotype.Component;

@Component
public class SpeciesAnalysisCounterAdapter implements Adapter<SpeciesAnalysis, SpeciesAnalysisCounter> {

    @Override
    public SpeciesAnalysisCounter adapt(final SpeciesAnalysis argument) {
        return SpeciesAnalysisCounter.of(argument.getIdentifier());
    }
}
