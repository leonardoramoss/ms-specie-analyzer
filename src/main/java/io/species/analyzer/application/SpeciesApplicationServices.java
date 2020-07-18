package io.species.analyzer.application;

import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.SpecieRepository;
import io.species.analyzer.domain.species.Species;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import io.species.analyzer.infrastructure.annotation.ApplicationServices;
import io.species.analyzer.infrastructure.exception.SpecieException;
import io.species.analyzer.infrastructure.generator.SpecieUUIDGenerator;
import io.species.analyzer.infrastructure.generator.UUIDGenerator;

import java.util.EnumMap;
import java.util.Map;

@ApplicationServices
public class SpeciesApplicationServices {

    private final SpecieRepository specieRepository;
    private final Map<Species, Analyzer> analyzers = new EnumMap<>(Species.class);
    private final UUIDGenerator<Specie> uuidGenerator = new SpecieUUIDGenerator();
    private final Analyzer defaultAnalyzer = (final Specie s) -> { throw new SpecieException(""); };

    public SpeciesApplicationServices(final SpecieRepository specieRepository, final Map<Species, Analyzer> analyzers) {
        this.specieRepository = specieRepository;
        this.analyzers.putAll(analyzers);
    }

    public Specie analyzeSpecie(final Specie specie) {
        final var dnaSpecieUUID = uuidGenerator.generate(specie);

        final var optionalSpecie = specieRepository.findById(dnaSpecieUUID);
        if(optionalSpecie.isPresent()) {
            return optionalSpecie.get();
        }

        final var analyzer = analyzers.getOrDefault(specie.getExpected(), defaultAnalyzer);
        final var specieAnalyzed = analyzer.analyze(specie);

        specieRepository.save(specieAnalyzed.withUUID(dnaSpecieUUID));

        return specie;
    }
}
