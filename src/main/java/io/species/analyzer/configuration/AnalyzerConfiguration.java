package io.species.analyzer.configuration;

import io.species.analyzer.domain.species.SpeciesIdentifier;
import io.species.analyzer.domain.species.analyzer.PrimateAnalyzer;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AnalyzerConfiguration {

    @Bean
    public Map<SpeciesIdentifier, Analyzer> analyzers(final PrimateAnalyzer primateAnalyzer) {
        return Map.of(SpeciesIdentifier.SIMIAN, primateAnalyzer);
    }
}
