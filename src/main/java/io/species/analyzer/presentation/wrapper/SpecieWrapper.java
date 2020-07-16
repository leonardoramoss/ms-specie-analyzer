package io.species.analyzer.presentation.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.infrastructure.serialization.deserializer.SpecieDeserializer;

import java.util.Optional;

@JsonDeserialize(using = SpecieDeserializer.class)
public class SpecieWrapper {

    private final Optional<Specie> specie;

    public SpecieWrapper(final Specie specie) {
        this.specie = Optional.ofNullable(specie);
    }

    public Optional<Specie> getSpecie() {
        return this.specie;
    }
}
