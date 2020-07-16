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
public class SpecieController {

    private final SpeciesApplicationServices speciesApplicationServices;

    public SpecieController(final SpeciesApplicationServices speciesApplicationServices) {
        this.speciesApplicationServices = speciesApplicationServices;
    }

    @RequestMapping(value = "/simian", method = RequestMethod.POST)
    public void simian(@RequestBody final SpecieWrapper specieWrapper) {
        final Optional<Specie> optionalSpecie = specieWrapper.getSpecie();
        final Specie specie = optionalSpecie.orElseThrow(() -> new SimianException(""));
        speciesApplicationServices.analyzeSimian(specie.markCandidateFor(Species.SIMIAN));
    }
}
