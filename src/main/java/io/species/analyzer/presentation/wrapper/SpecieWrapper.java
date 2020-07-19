package io.species.analyzer.presentation.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.infrastructure.serialization.deserializer.SpecieDeserializer;

import java.util.Optional;

@JsonDeserialize(using = SpecieDeserializer.class)
public class SpecieWrapper {

    private final Optional<SpeciesAnalysis> specie;

    public SpecieWrapper(final SpeciesAnalysis speciesAnalysis) {
        this.specie = Optional.ofNullable(speciesAnalysis);
    }

    public Optional<SpeciesAnalysis> getSpecie() {
        return this.specie;
    }
}
