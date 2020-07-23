package io.species.analyzer.infrastructure.generator;

import io.species.analyzer.domain.species.SpeciesAnalysisCounter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpecieAnalysisCounterUUIDGenerator implements UUIDGenerator<SpeciesAnalysisCounter> {

    @Override
    public UUID generate(final SpeciesAnalysisCounter analysisCounter) {
        final var identifier = analysisCounter.getIdentifier().toString();
        return UUID.nameUUIDFromBytes(identifier.getBytes());
    }
}
