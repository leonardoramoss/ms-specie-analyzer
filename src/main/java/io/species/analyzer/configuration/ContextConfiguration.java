package io.species.analyzer.configuration;

import io.species.analyzer.domain.event.EventListener;
import io.species.analyzer.domain.event.EventType;
import io.species.analyzer.domain.species.SpecieAnalyzedCounterEventListener;
import io.species.analyzer.domain.species.SpecieAnalyzedEventListener;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import io.species.analyzer.domain.species.analyzer.SimianAnalyzer;
import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.stats.StatsExecutor;
import io.species.analyzer.domain.species.stats.StatsHumanSimianRatioExecutor;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import io.species.analyzer.infrastructure.auditing.filter.AuditableRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ContextConfiguration {

    private static final Integer AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(AVAILABLE_PROCESSORS);
    }

    @Bean
    public Map<Specie, Analyzer> analyzers(final SimianAnalyzer simianAnalyzer) {
        return Map.of(Specie.SIMIAN, simianAnalyzer);
    }

    @Bean
    public Map<StatsIdentifier, StatsExecutor> statsExecutors(final StatsHumanSimianRatioExecutor statsHumanSimianRatioExecutor) {
        return Map.of(StatsIdentifier.RATIO_SIMIAN_HUMAN, statsHumanSimianRatioExecutor);
    }

    @Bean
    public Map<EventType, List<EventListener>> listeners(final SpecieAnalyzedEventListener specieAnalyzedEventListener,
                                                         final SpecieAnalyzedCounterEventListener specieAnalyzedCounterEventListener) {
        return Map.of(EventType.SPECIE_ANALYZED, Arrays.asList(specieAnalyzedEventListener, specieAnalyzedCounterEventListener));
    }

    @Bean
    public FilterRegistrationBean<AuditableRequestFilter> logFilter(final AuditableRequestFilter auditableRequestFilter) {
        final var registrationBean = new FilterRegistrationBean<AuditableRequestFilter>();
        registrationBean.setFilter(auditableRequestFilter);
        return registrationBean;
    }
}
