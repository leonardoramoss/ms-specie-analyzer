package io.species.analyzer.presentation.wrapper;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.species.analyzer.domain.species.stats.StatsResult;
import io.species.analyzer.infrastructure.serialization.serializer.StatsResultWrapperSerializer;

import java.util.Optional;

@JsonSerialize(using = StatsResultWrapperSerializer.class)
public class StatsResultWrapper {

    private final Optional<StatsResult> stats;

    public StatsResultWrapper(final StatsResult stats) {
        this.stats = Optional.ofNullable(stats);
    }

    public Optional<StatsResult> getStats() {
        return stats;
    }
}
