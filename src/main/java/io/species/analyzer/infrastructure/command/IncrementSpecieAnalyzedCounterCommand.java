package io.species.analyzer.infrastructure.command;

import io.species.analyzer.domain.species.SpecieAnalysisCounterRepository;
import io.species.analyzer.domain.species.SpecieAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisCounter;
import io.species.analyzer.infrastructure.adapter.Adapter;
import io.species.analyzer.infrastructure.generator.SpecieAnalysisCounterUUIDGenerator;
import org.springframework.stereotype.Component;

@Component
public class IncrementSpecieAnalyzedCounterCommand implements Command<SpecieAnalysis> {

    private final SpecieAnalysisCounterRepository specieAnalysisCounterRepository;
    private final SpecieAnalysisCounterUUIDGenerator uuidGenerator;
    private final Adapter<SpecieAnalysis, SpeciesAnalysisCounter> adapter;

    public IncrementSpecieAnalyzedCounterCommand(final SpecieAnalysisCounterRepository specieAnalysisCounterRepository,
                                                 final SpecieAnalysisCounterUUIDGenerator uuidGenerator,
                                                 final Adapter<SpecieAnalysis, SpeciesAnalysisCounter> adapter) {
        this.specieAnalysisCounterRepository = specieAnalysisCounterRepository;
        this.uuidGenerator = uuidGenerator;
        this.adapter = adapter;
    }

    @Override
    public synchronized void execute(final SpecieAnalysis specieAnalysis) {
        final int updatedSpeciesCounter = specieAnalysisCounterRepository.incrementSpecieCounter(specieAnalysis.getSpecie());

        if(updatedSpeciesCounter != 1) {
            final var speciesAnalysisCounter = adapter.adapt(specieAnalysis);
            specieAnalysisCounterRepository.save(speciesAnalysisCounter.withUUID(uuidGenerator));
        }
    }
}
