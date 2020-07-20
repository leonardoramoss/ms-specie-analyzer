package io.species.analyzer.infrastructure.retrieve;

import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import io.species.analyzer.infrastructure.generator.UUIDGenerator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpeciesAnalysisRepositoryRetriever implements Retriever<SpeciesAnalysis, Optional<SpeciesAnalysis>> {

    private final SpeciesAnalysisRepository speciesAnalysisRepository;
    private final UUIDGenerator<SpeciesAnalysis> uuidGenerator;

    public SpeciesAnalysisRepositoryRetriever(final SpeciesAnalysisRepository speciesAnalysisRepository,
                                              final UUIDGenerator<SpeciesAnalysis> uuidGenerator) {
        this.speciesAnalysisRepository = speciesAnalysisRepository;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public Optional<SpeciesAnalysis> retrieve(final SpeciesAnalysis speciesAnalysis) {
        return speciesAnalysisRepository.findById(uuidGenerator.generate(speciesAnalysis));
    }
}
