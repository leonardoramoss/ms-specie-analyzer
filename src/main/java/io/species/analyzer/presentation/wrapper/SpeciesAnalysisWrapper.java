package io.species.analyzer.presentation.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.infrastructure.serialization.deserializer.SpeciesAnalysisWrapperDeserializer;

import java.util.Optional;

@JsonDeserialize(using = SpeciesAnalysisWrapperDeserializer.class)
public class SpeciesAnalysisWrapper {

    private final Optional<SpeciesAnalysis> specie;

    public SpeciesAnalysisWrapper(final SpeciesAnalysis speciesAnalysis) {
        this.specie = Optional.ofNullable(speciesAnalysis);
    }

    public Optional<SpeciesAnalysis> getSpecie() {
        return this.specie;
    }
}
