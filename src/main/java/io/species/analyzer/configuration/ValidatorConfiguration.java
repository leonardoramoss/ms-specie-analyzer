package io.species.analyzer.configuration;

import io.species.analyzer.domain.species.Species;
import io.species.analyzer.domain.species.analyzer.SimianAnalyzer;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ValidatorConfiguration {

    @Bean
    public Map<Species, Analyzer> validatorMap(final SimianAnalyzer simianValidator) {
        return Map.of(Species.SIMIAN, simianValidator);
    }
}
