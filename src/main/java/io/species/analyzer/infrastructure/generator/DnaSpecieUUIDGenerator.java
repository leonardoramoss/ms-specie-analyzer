package io.species.analyzer.infrastructure.generator;

import io.species.analyzer.domain.species.SpecieAnalysis;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DnaSpecieUUIDGenerator implements UUIDGenerator<SpecieAnalysis> {

    @Override
    public UUID generate(final SpecieAnalysis specieAnalysis) {
        return UUID.nameUUIDFromBytes(specieAnalysis.getDna().getBytes());
    }
}
