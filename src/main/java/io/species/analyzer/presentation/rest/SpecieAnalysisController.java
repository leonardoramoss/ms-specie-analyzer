package io.species.analyzer.presentation.rest;

import io.species.analyzer.application.SpeciesApplicationServices;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesIdentifier;
import io.species.analyzer.infrastructure.exception.SimianException;
import io.species.analyzer.presentation.wrapper.SpeciesAnalysisWrapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class SpecieAnalysisController {

    private final SpeciesApplicationServices speciesApplicationServices;

    public SpecieAnalysisController(final SpeciesApplicationServices speciesApplicationServices) {
        this.speciesApplicationServices = speciesApplicationServices;
    }

    @RequestMapping(value = "/simian", method = RequestMethod.POST)
    public void simian(@RequestBody final SpeciesAnalysisWrapper speciesAnalysisWrapper) {
        final Optional<SpeciesAnalysis> optionalSpecie = speciesAnalysisWrapper.getSpecie();
        optionalSpecie.map(specie -> speciesApplicationServices.analyzeSpecie(specie.markExpectedIdentifierAs(SpeciesIdentifier.SIMIAN)))
                .filter(SpeciesAnalysis::isIdentifierMatchesAsExpected)
                .orElseThrow(() -> new SimianException("Is not a simian"));
    }
}
