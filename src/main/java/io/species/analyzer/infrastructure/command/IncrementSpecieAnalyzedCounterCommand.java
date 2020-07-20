package io.species.analyzer.infrastructure.command;

import io.species.analyzer.domain.species.SpecieAnalysisCounterRepository;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisCounter;
import io.species.analyzer.infrastructure.adapter.Adapter;
import io.species.analyzer.infrastructure.generator.SpecieAnalysisCounterUUIDGenerator;
import org.springframework.stereotype.Component;

@Component
public class IncrementSpecieAnalyzedCounterCommand implements Command<SpeciesAnalysis> {

    private final SpecieAnalysisCounterRepository specieAnalysisCounterRepository;
    private final SpecieAnalysisCounterUUIDGenerator uuidGenerator;
    private final Adapter<SpeciesAnalysis, SpeciesAnalysisCounter> adapter;

    public IncrementSpecieAnalyzedCounterCommand(final SpecieAnalysisCounterRepository specieAnalysisCounterRepository,
                                                 final SpecieAnalysisCounterUUIDGenerator uuidGenerator,
                                                 final Adapter<SpeciesAnalysis, SpeciesAnalysisCounter> adapter) {
        this.specieAnalysisCounterRepository = specieAnalysisCounterRepository;
        this.uuidGenerator = uuidGenerator;
        this.adapter = adapter;
    }

    @Override
    public synchronized void execute(final SpeciesAnalysis argument) {
        final var speciesAnalysisCounter = adapter.adapt(argument);
        final var uuid = uuidGenerator.generate(speciesAnalysisCounter);
        final var optionalCounter = specieAnalysisCounterRepository.findById(uuid);
        final var analysisCounter = optionalCounter.orElse(speciesAnalysisCounter);
        specieAnalysisCounterRepository.save(analysisCounter.withUUID(uuid).increment());
    }
}
