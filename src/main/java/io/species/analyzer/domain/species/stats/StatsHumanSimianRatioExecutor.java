package io.species.analyzer.domain.species.stats;

import io.species.analyzer.domain.species.SpeciesIdentifier;
import io.species.analyzer.infrastructure.retrieve.Retriever;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class StatsHumanSimianRatioExecutor implements StatsExecutor<StatsHumanSimianRatioResult> {

    private final Retriever<List<SpeciesIdentifier>, Map<SpeciesIdentifier, Long>> retriever;

    public StatsHumanSimianRatioExecutor(final Retriever<List<SpeciesIdentifier>, Map<SpeciesIdentifier, Long>> retriever) {
        this.retriever = retriever;
    }

    @Override
    public StatsHumanSimianRatioResult execute() {

        final var speciesCount = this.retriever.retrieve(Arrays.asList(SpeciesIdentifier.HUMAN, SpeciesIdentifier.SIMIAN));

        final var simianCount = speciesCount.getOrDefault(SpeciesIdentifier.SIMIAN, 0L);
        final var humanCount = speciesCount.getOrDefault(SpeciesIdentifier.HUMAN, 0L);

        var ratio = BigDecimal.ZERO;
        if (humanCount == 0L) {
            ratio = BigDecimal.valueOf(simianCount);
        } else {
            ratio = BigDecimal.valueOf((simianCount * 100.0f) / humanCount)
                    .divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP);
        }

        return new StatsHumanSimianRatioResult(humanCount, simianCount, ratio);
    }
}
