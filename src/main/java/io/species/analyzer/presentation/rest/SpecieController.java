package io.species.analyzer.presentation.rest;

import io.species.analyzer.application.SpeciesApplicationServices;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import io.species.analyzer.domain.species.stats.StatsResult;
import io.species.analyzer.infrastructure.exception.SimianException;
import io.species.analyzer.presentation.wrapper.SpecieWrapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class SpecieController {

    private final SpeciesApplicationServices speciesApplicationServices;

    public SpecieController(final SpeciesApplicationServices speciesApplicationServices) {
        this.speciesApplicationServices = speciesApplicationServices;
    }

    @RequestMapping(value = "/simian", method = RequestMethod.POST)
    public SpeciesAnalysis simian(@RequestBody final SpecieWrapper specieWrapper) {
        final Optional<SpeciesAnalysis> optionalSpecie = specieWrapper.getSpecie();
        return optionalSpecie.map(specie -> speciesApplicationServices.analyzeSpecie(specie.markExpectedIdentifierAs(SpeciesIdentifier.SIMIAN)))
                .filter(SpeciesAnalysis::isIdentifierMatchesAsExpected)
                .orElseThrow(() -> new SimianException(""));
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public StatsResult stats() {
        return this.speciesApplicationServices.viewStats(StatsIdentifier.RATIO_SIMIAN_HUMAN);
    }
}
