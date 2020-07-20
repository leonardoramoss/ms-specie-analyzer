package io.species.analyzer.infrastructure.retrieve;

import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatsCounterHumanSimianRepositoryRetriever implements Retriever<List<SpeciesIdentifier>, Map<SpeciesIdentifier, Long>> {

    private final SpeciesAnalysisRepository speciesAnalysisRepository;

    public StatsCounterHumanSimianRepositoryRetriever(final SpeciesAnalysisRepository speciesAnalysisRepository) {
        this.speciesAnalysisRepository = speciesAnalysisRepository;
    }

    @Override
    public Map<SpeciesIdentifier, Long> retrieve(final List<SpeciesIdentifier> speciesIdentifiers) {
        final var countSpeciesAnalysesByIdentifier = speciesAnalysisRepository.countSpeciesAnalysesByIdentifierIn(speciesIdentifiers);
        return countSpeciesAnalysesByIdentifier.stream()
                .collect(Collectors.toMap(specieIdentifier -> (SpeciesIdentifier) specieIdentifier[0], count -> (Long) count[1]));
    }
}
