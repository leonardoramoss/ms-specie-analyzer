package io.species.analyzer.presentation.rest;

import io.species.analyzer.application.SpeciesApplicationServices;
import io.species.analyzer.presentation.wrapper.SpecieAnalysisWrapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class SpecieAnalysisController {

    private final SpeciesApplicationServices speciesApplicationServices;

    public SpecieAnalysisController(final SpeciesApplicationServices speciesApplicationServices) {
        this.speciesApplicationServices = speciesApplicationServices;
    }

    @RequestMapping(value = "/analyze/{specie}", method = RequestMethod.POST)
    public void analyze(@RequestBody final SpecieAnalysisWrapper specieAnalysisWrapper, @PathVariable("specie") final String specie) {
        specieAnalysisWrapper.getSpecie()
                .map(it -> speciesApplicationServices.analyzeSpecie(it.expectedSpecieAs(specie)));
    }
}
