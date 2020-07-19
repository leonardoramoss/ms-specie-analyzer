package io.species.analyzer.infrastructure.generator;

import io.species.analyzer.domain.species.SpeciesAnalysis;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DnaSpecieUUIDGenerator implements UUIDGenerator<SpeciesAnalysis> {

    @Override
    public UUID generate(final SpeciesAnalysis speciesAnalysis) {
        return UUID.nameUUIDFromBytes(speciesAnalysis.getDna().getBytes());
    }
}
