package io.species.analyzer.application;

import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.SpecieRepository;
import io.species.analyzer.domain.species.Species;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import io.species.analyzer.infrastructure.annotation.ApplicationServices;
import io.species.analyzer.infrastructure.exception.SimianException;
import io.species.analyzer.infrastructure.exception.SpecieException;
import io.species.analyzer.infrastructure.generator.SpecieUUIDGenerator;
import io.species.analyzer.infrastructure.generator.UUIDGenerator;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    public void analyzeSimian(final Specie specie) {
        final Specie validateDNA = this.analyzeSpecie(specie);
        final boolean isSimian = validateDNA.isSpeciesOf(Species.SIMIAN);

        if(!isSimian)
            throw new SimianException("");
    }

    public Specie analyzeSpecie(final Specie specie) {
        final UUID dnaSpecieUUID = uuidGenerator.generate(specie);

        final Optional<Specie> optionalSpecie = specieRepository.findById(dnaSpecieUUID);
        if(optionalSpecie.isPresent()) {
            return optionalSpecie.get();
        }

        final Analyzer analyzer = analyzers.getOrDefault(specie.getCandidate(), defaultAnalyzer);
        final Specie specieAnalyzed = analyzer.analyze(specie);
        return specieRepository.save(specieAnalyzed.withUUID(dnaSpecieUUID));
    }
}
