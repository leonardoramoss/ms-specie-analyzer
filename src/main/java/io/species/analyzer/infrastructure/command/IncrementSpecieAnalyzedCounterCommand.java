package io.species.analyzer.infrastructure.command;

import io.species.analyzer.domain.species.SpecieAnalysisCounterRepository;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.domain.species.SpeciesAnalysisCounter;
import io.species.analyzer.infrastructure.adapter.Adapter;
import io.species.analyzer.infrastructure.generator.SpecieAnalysisCounterUUIDGenerator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class IncrementSpecieAnalyzedCounterCommand implements Command<SpeciesAnalysis> {

    private final SpecieAnalysisCounterRepository specieAnalysisCounterRepository;
    private final SpecieAnalysisCounterUUIDGenerator uuidGenerator;
    private final Adapter<SpeciesAnalysis, SpeciesAnalysisCounter> adapter;
    private final ExecutorService executorService;

    public IncrementSpecieAnalyzedCounterCommand(final SpecieAnalysisCounterRepository specieAnalysisCounterRepository,
                                                 final SpecieAnalysisCounterUUIDGenerator uuidGenerator,
                                                 final Adapter<SpeciesAnalysis, SpeciesAnalysisCounter> adapter,
                                                 final ExecutorService executorService) {
        this.specieAnalysisCounterRepository = specieAnalysisCounterRepository;
        this.uuidGenerator = uuidGenerator;
        this.adapter = adapter;
        this.executorService = executorService;
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
