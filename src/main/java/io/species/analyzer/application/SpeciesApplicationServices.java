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
import io.species.analyzer.infrastructure.exception.SpecieAnalyzerException;
import io.species.analyzer.infrastructure.exception.StatsExecutorException;
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
        final var optionalAnalyzedSpecie = retriever.retrieve(speciesAnalysis);
        final Analyzer defaultAnalyzer = (final SpeciesAnalysis s) -> { throw new SpecieAnalyzerException("There are no analyzer for this specie: " + s.getExpectedIdentifier()); };

        return optionalAnalyzedSpecie.orElseGet(() -> {
            final var analyzer = analyzers.getOrDefault(speciesAnalysis.getExpectedIdentifier(), defaultAnalyzer);
            final var specieAnalyzed = analyzer.analyze(speciesAnalysis);
            eventNotifier.notify(SpecieAnalyzedEvent.of(specieAnalyzed));
            return specieAnalyzed;
        });
    }

    public StatsResult viewStats(final StatsIdentifier statsIdentifier) {
        final StatsExecutor defaultStatsExecutor = () -> { throw new StatsExecutorException("There are no stats executor configured: " + statsIdentifier.name()); };
        final var statsExecutor = executors.getOrDefault(statsIdentifier, defaultStatsExecutor);
        return statsExecutor.execute();
    }
}
