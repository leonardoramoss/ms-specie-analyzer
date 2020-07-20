package io.species.analyzer.presentation.rest;

import io.species.analyzer.application.SpeciesApplicationServices;
import io.species.analyzer.domain.species.stats.StatsIdentifier;
import io.species.analyzer.presentation.wrapper.StatsResultWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class SpecieStatsController {

    private final SpeciesApplicationServices speciesApplicationServices;

    public SpecieStatsController(final SpeciesApplicationServices speciesApplicationServices) {
        this.speciesApplicationServices = speciesApplicationServices;
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public StatsResultWrapper stats() {
        final var result = this.speciesApplicationServices.viewStats(StatsIdentifier.RATIO_SIMIAN_HUMAN);
        return new StatsResultWrapper(result);
    }
}
