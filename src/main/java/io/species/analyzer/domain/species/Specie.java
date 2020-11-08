package io.species.analyzer.domain.species;

import io.species.analyzer.infrastructure.exception.SpecieNotFoundException;

import java.util.Arrays;

public enum Specie {

    SIMIAN,
    HUMAN,
    NOT_IDENTIFIED;

    public static Specie byName(final String name) {
        return Arrays.stream(values())
                .filter(s -> s.name().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new SpecieNotFoundException("Specie not found"));
    }
}
