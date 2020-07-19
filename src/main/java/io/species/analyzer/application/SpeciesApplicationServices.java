package io.species.analyzer.application;

import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import io.species.analyzer.domain.species.stats.StatsExecutor;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import io.species.analyzer.domain.species.stats.StatsResult;
import io.species.analyzer.infrastructure.annotation.ApplicationServices;
import io.species.analyzer.infrastructure.exception.SpecieException;
import io.species.analyzer.infrastructure.generator.DnaSpecieUUIDGenerator;

import java.util.EnumMap;
import java.util.Map;

@ApplicationServices
public class SpeciesApplicationServices {

    private final SpeciesAnalysisRepository speciesAnalysisRepository;
    private final Map<SpeciesIdentifier, Analyzer> analyzers = new EnumMap<>(SpeciesIdentifier.class);
    private final Map<StatsIdentifier, StatsExecutor> executors = new EnumMap<>(StatsIdentifier.class);

    public SpeciesApplicationServices(final SpeciesAnalysisRepository speciesAnalysisRepository,
                                      final Map<SpeciesIdentifier, Analyzer> analyzers,
                                      final Map<StatsIdentifier, StatsExecutor> executors) {
        this.speciesAnalysisRepository = speciesAnalysisRepository;
        this.analyzers.putAll(analyzers);
        this.executors.putAll(executors);
    }

    public SpeciesAnalysis analyzeSpecie(final SpeciesAnalysis speciesAnalysis) {
        final var uuidGenerator = new DnaSpecieUUIDGenerator();
        final var dnaSpecieUUID = uuidGenerator.generate(speciesAnalysis);

        final var optionalSpecie = speciesAnalysisRepository.findById(dnaSpecieUUID);
        if(optionalSpecie.isPresent()) {
            return optionalSpecie.get();
        }

        final var analyzer = analyzers.getOrDefault(speciesAnalysis.getExpectedIdentifier(), (final SpeciesAnalysis s) -> { throw new SpecieException(""); });
        final var specieAnalyzed = analyzer.analyze(speciesAnalysis);

        speciesAnalysisRepository.save(specieAnalyzed.withUUID(dnaSpecieUUID));

        return specieAnalyzed;
    }

    public StatsResult viewStats(final StatsIdentifier statsIdentifier) {
        return executors.getOrDefault(statsIdentifier, () -> { throw new SpecieException(""); }).execute();
    }
}
