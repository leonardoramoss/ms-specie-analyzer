package io.species.analyzer.configuration;

import io.species.analyzer.domain.species.Species;
import io.species.analyzer.domain.species.analyzer.PrimateAnalyzer;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AnalyzerConfiguration {

    @Bean
    public Map<Species, Analyzer> validatorMap(final PrimateAnalyzer primateAnalyzer) {
        return Map.of(Species.SIMIAN, primateAnalyzer);
    }
}
