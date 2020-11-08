package io.species.analyzer.infrastructure.retrieve;

import io.species.analyzer.domain.species.SpecieAnalysisCounterRepository;
import io.species.analyzer.domain.species.SpeciesAnalysisCounter;
import io.species.analyzer.domain.species.Specie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatsCounterHumanSimianRepositoryFetcher implements Fetcher<List<Specie>, Map<Specie, Long>> {

    private final SpecieAnalysisCounterRepository specieAnalysisCounterRepository;

    public StatsCounterHumanSimianRepositoryFetcher(final SpecieAnalysisCounterRepository specieAnalysisCounterRepository) {
        this.specieAnalysisCounterRepository = specieAnalysisCounterRepository;
    }

    @Override
    public Map<Specie, Long> fetch(final List<Specie> species) {
        final var speciesAnalysisCounters = specieAnalysisCounterRepository.findAllBySpecieIn(species);
        return speciesAnalysisCounters.stream()
                .collect(Collectors.toMap(SpeciesAnalysisCounter::getSpecie, SpeciesAnalysisCounter::getCounter));

    }
}
