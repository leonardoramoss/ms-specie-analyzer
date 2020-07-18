package io.species.analyzer.presentation.rest;

import io.species.analyzer.application.SpeciesApplicationServices;
import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.Species;
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
    public Specie simian(@RequestBody final SpecieWrapper specieWrapper) {
        final Optional<Specie> optionalSpecie = specieWrapper.getSpecie();
        return optionalSpecie.map(specie -> speciesApplicationServices.analyzeSpecie(specie.markExpectedSpecieAs(Species.SIMIAN)))
                .filter(Specie::isSpecieMatchesAsExpected)
                .orElseThrow(() -> new SimianException(""));
    }
}
