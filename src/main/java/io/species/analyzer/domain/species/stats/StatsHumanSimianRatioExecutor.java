package io.species.analyzer.domain.species.stats;

import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisRepository;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatsHumanSimianRatioExecutor implements StatsExecutor<StatsHumanSimianRatioResult> {

    private final SpeciesAnalysisRepository speciesAnalysisRepository;

    public StatsHumanSimianRatioExecutor(final SpeciesAnalysisRepository speciesAnalysisRepository) {
        this.speciesAnalysisRepository = speciesAnalysisRepository;
    }

    @Override
    public StatsHumanSimianRatioResult execute() {

        final List<SpeciesAnalysis> speciesAnalyses = speciesAnalysisRepository.findAll();

        final Map<SpeciesIdentifier, Long> speciesCount = speciesAnalyses.stream()
                .collect(Collectors.groupingBy(SpeciesAnalysis::getIdentifier, Collectors.counting()));

        final var simianCount = speciesCount.getOrDefault(SpeciesIdentifier.SIMIAN, 0L);
        final var humanCount = speciesCount.getOrDefault(SpeciesIdentifier.HUMAN, 0L);

        final var ratio = BigDecimal.valueOf((simianCount * 100.0f) / humanCount)
                .divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP);

        return new StatsHumanSimianRatioResult(humanCount, simianCount, ratio);
    }
}
