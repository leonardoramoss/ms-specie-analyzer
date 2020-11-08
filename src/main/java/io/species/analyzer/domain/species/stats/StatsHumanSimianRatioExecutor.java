package io.species.analyzer.domain.species.stats;

import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.infrastructure.retrieve.Fetcher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class StatsHumanSimianRatioExecutor implements StatsExecutor<StatsHumanSimianRatioResult> {

    private final Fetcher<List<Specie>, Map<Specie, Long>> fetcher;

    public StatsHumanSimianRatioExecutor(final Fetcher<List<Specie>, Map<Specie, Long>> fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public StatsHumanSimianRatioResult execute() {

        final var speciesCount = this.fetcher.fetch(Arrays.asList(Specie.HUMAN, Specie.SIMIAN));

        final var simianCount = speciesCount.getOrDefault(Specie.SIMIAN, 0L);
        final var humanCount = speciesCount.getOrDefault(Specie.HUMAN, 0L);

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
