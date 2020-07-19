package io.species.analyzer.configuration;

import io.species.analyzer.domain.species.stats.StatsExecutor;
import io.species.analyzer.domain.species.stats.StatsHumanSimianRatioExecutor;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StatsExecutorConfiguration {

    @Bean
    public Map<StatsIdentifier, StatsExecutor> statsExecutors(final StatsHumanSimianRatioExecutor statsHumanSimianRatioExecutor) {
        return Map.of(StatsIdentifier.RATIO_SIMIAN_HUMAN, statsHumanSimianRatioExecutor);
    }
}
