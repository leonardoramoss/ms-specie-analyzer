package io.species.analyzer.presentation.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.species.analyzer.domain.species.SpecieAnalysis;
import io.species.analyzer.infrastructure.serialization.deserializer.SpeciesAnalysisWrapperDeserializer;

import java.util.Optional;

@JsonDeserialize(using = SpeciesAnalysisWrapperDeserializer.class)
public class SpecieAnalysisWrapper {

    private final Optional<SpecieAnalysis> specie;

    public SpecieAnalysisWrapper(final SpecieAnalysis specieAnalysis) {
        this.specie = Optional.ofNullable(specieAnalysis);
    }

    public Optional<SpecieAnalysis> getSpecie() {
        return this.specie;
    }
}
