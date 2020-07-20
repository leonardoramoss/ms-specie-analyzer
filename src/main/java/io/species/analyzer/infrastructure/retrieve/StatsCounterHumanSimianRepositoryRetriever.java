package io.species.analyzer.infrastructure.retrieve;

import io.species.analyzer.domain.species.SpecieAnalysisCounterRepository;
import io.species.analyzer.domain.species.SpeciesAnalysisCounter;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatsCounterHumanSimianRepositoryRetriever implements Retriever<List<SpeciesIdentifier>, Map<SpeciesIdentifier, Long>> {

    private final SpecieAnalysisCounterRepository specieAnalysisCounterRepository;

    public StatsCounterHumanSimianRepositoryRetriever(final SpecieAnalysisCounterRepository specieAnalysisCounterRepository) {
        this.specieAnalysisCounterRepository = specieAnalysisCounterRepository;
    }

    @Override
    public Map<SpeciesIdentifier, Long> retrieve(final List<SpeciesIdentifier> speciesIdentifiers) {
        final var speciesAnalysisCounters = specieAnalysisCounterRepository.findAllByIdentifierIn(speciesIdentifiers);
        return speciesAnalysisCounters.stream()
                .collect(Collectors.toMap(SpeciesAnalysisCounter::getIdentifier, SpeciesAnalysisCounter::getCounter));

    }
}
