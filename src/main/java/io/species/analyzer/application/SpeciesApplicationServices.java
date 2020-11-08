package io.species.analyzer.application;

import io.species.analyzer.domain.event.DomainEvent;
import io.species.analyzer.domain.event.EventNotifier;
import io.species.analyzer.domain.species.SpecieAnalysis;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.stats.StatsExecutor;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import io.species.analyzer.domain.species.stats.StatsResult;
import io.species.analyzer.infrastructure.annotation.ApplicationServices;
import io.species.analyzer.infrastructure.exception.SpecieAnalyzerException;
import io.species.analyzer.infrastructure.exception.StatsExecutorException;
import io.species.analyzer.infrastructure.retrieve.Fetcher;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@ApplicationServices
public class SpeciesApplicationServices {

    private final Map<Specie, Analyzer> analyzers = new EnumMap<>(Specie.class);
    private final Map<StatsIdentifier, StatsExecutor> executors = new EnumMap<>(StatsIdentifier.class);

    private final EventNotifier<DomainEvent> eventNotifier;
    private final Fetcher<SpecieAnalysis, Optional<SpecieAnalysis>> fetcher;

    public SpeciesApplicationServices(final Map<Specie, Analyzer> analyzers,
                                      final Map<StatsIdentifier, StatsExecutor> executors,
                                      final EventNotifier<DomainEvent> eventNotifier,
                                      final Fetcher<SpecieAnalysis, Optional<SpecieAnalysis>> fetcher) {
        this.fetcher = fetcher;
        this.analyzers.putAll(analyzers);
        this.executors.putAll(executors);
        this.eventNotifier = eventNotifier;
    }

    public SpecieAnalysis analyzeSpecie(final SpecieAnalysis specieAnalysis) {
        final var optionalSpecieAnalyzed = fetcher.fetch(specieAnalysis);
        return optionalSpecieAnalyzed.orElseGet(() -> {
            final Analyzer defaultAnalyzer = (SpecieAnalysis s) -> { throw new SpecieAnalyzerException("There are no analyzer for this specie: " + s.getExpectedSpecie()); };
            final var analyzer = analyzers.getOrDefault(specieAnalysis.getExpectedSpecie(), defaultAnalyzer);
            return specieAnalysis.with(analyzer, eventNotifier::notify);
        });
    }

    public StatsResult viewStats(final StatsIdentifier statsIdentifier) {
        final StatsExecutor defaultStatsExecutor = () -> { throw new StatsExecutorException("There are no stats executor configured: " + statsIdentifier.name()); };
        final var statsExecutor = executors.getOrDefault(statsIdentifier, defaultStatsExecutor);
        return statsExecutor.execute();
    }
}
