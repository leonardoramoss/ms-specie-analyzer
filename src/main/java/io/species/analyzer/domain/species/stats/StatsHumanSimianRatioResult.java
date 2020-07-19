package io.species.analyzer.domain.species.stats;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.species.analyzer.infrastructure.serialization.serializer.StatsHumanSimianRatioResultSerializer;

import java.math.BigDecimal;

@JsonSerialize(using = StatsHumanSimianRatioResultSerializer.class)
public class StatsHumanSimianRatioResult implements StatsResult<BigDecimal> {

    private final Long humanCount;
    private final Long simianCount;
    private final BigDecimal ratio;

    public StatsHumanSimianRatioResult(final Long humanCount, final Long simianCount, final BigDecimal ratio) {
        this.humanCount = humanCount;
        this.simianCount = simianCount;
        this.ratio = ratio;
    }

    public Long getHumanCount() {
        return humanCount;
    }

    public Long getSimianCount() {
        return simianCount;
    }

    @Override
    public BigDecimal result() {
        return ratio;
    }
}
