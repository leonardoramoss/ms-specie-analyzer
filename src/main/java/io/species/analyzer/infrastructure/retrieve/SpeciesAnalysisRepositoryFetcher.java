package io.species.analyzer.infrastructure.retrieve;

import io.species.analyzer.domain.species.SpecieAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import io.species.analyzer.infrastructure.generator.UUIDGenerator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpeciesAnalysisRepositoryFetcher implements Fetcher<SpecieAnalysis, Optional<SpecieAnalysis>> {

    private final SpeciesAnalysisRepository speciesAnalysisRepository;
    private final UUIDGenerator<SpecieAnalysis> uuidGenerator;

    public SpeciesAnalysisRepositoryFetcher(final SpeciesAnalysisRepository speciesAnalysisRepository,
                                            final UUIDGenerator<SpecieAnalysis> uuidGenerator) {
        this.speciesAnalysisRepository = speciesAnalysisRepository;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public Optional<SpecieAnalysis> fetch(final SpecieAnalysis specieAnalysis) {
        return speciesAnalysisRepository.findById(uuidGenerator.generate(specieAnalysis));
    }
}
