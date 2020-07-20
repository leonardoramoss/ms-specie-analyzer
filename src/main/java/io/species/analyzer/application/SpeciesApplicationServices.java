package io.species.analyzer.application;

import io.species.analyzer.domain.event.DomainEvent;
import io.species.analyzer.domain.event.EventNotifier;
import io.species.analyzer.domain.event.SpecieAnalyzedEvent;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import io.species.analyzer.domain.species.stats.StatsExecutor;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import io.species.analyzer.domain.species.stats.StatsResult;
import io.species.analyzer.infrastructure.annotation.ApplicationServices;
import io.species.analyzer.infrastructure.exception.SpecieException;
import io.species.analyzer.infrastructure.retrieve.Retriever;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@ApplicationServices
public class SpeciesApplicationServices {

    private final Map<SpeciesIdentifier, Analyzer> analyzers = new EnumMap<>(SpeciesIdentifier.class);
    private final Map<StatsIdentifier, StatsExecutor> executors = new EnumMap<>(StatsIdentifier.class);

    private final EventNotifier<DomainEvent> eventNotifier;
    private final Retriever<SpeciesAnalysis, Optional<SpeciesAnalysis>> retriever;

    public SpeciesApplicationServices(final Map<SpeciesIdentifier, Analyzer> analyzers,
                                      final Map<StatsIdentifier, StatsExecutor> executors,
                                      final EventNotifier<DomainEvent> eventNotifier,
                                      final Retriever<SpeciesAnalysis, Optional<SpeciesAnalysis>> retriever) {
        this.eventNotifier = eventNotifier;
        this.retriever = retriever;
        this.analyzers.putAll(analyzers);
        this.executors.putAll(executors);
    }

    public SpeciesAnalysis analyzeSpecie(final SpeciesAnalysis speciesAnalysis) {
        final var optionalSpecie = retriever.retrieve(speciesAnalysis);
        if(optionalSpecie.isPresent()) {
            return optionalSpecie.get();
        }

        final var analyzer = analyzers.getOrDefault(speciesAnalysis.getExpectedIdentifier(),
                (final SpeciesAnalysis s) -> { throw new SpecieException("There are no analyzer for this specie: " + s.getIdentifier()); });
        final var specieAnalyzed = analyzer.analyze(speciesAnalysis);

        eventNotifier.notify(SpecieAnalyzedEvent.of(specieAnalyzed));

        return specieAnalyzed;
    }

    public StatsResult viewStats(final StatsIdentifier statsIdentifier) {
        return executors.getOrDefault(statsIdentifier, () -> { throw new SpecieException("There are no stats executor for this identifier: " + statsIdentifier.name()); }).execute();
    }
}
