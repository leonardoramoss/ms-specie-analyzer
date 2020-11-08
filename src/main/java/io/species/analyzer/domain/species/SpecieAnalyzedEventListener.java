package io.species.analyzer.domain.species;

import io.species.analyzer.domain.event.EventListener;
import io.species.analyzer.domain.event.SpecieAnalyzedEvent;
import io.species.analyzer.infrastructure.generator.UUIDGenerator;
import org.springframework.stereotype.Component;

@Component
public class SpecieAnalyzedEventListener implements EventListener<SpecieAnalyzedEvent> {

    private final SpeciesAnalysisRepository analysisRepository;
    private final UUIDGenerator<SpecieAnalysis> uuidGenerator;

    public SpecieAnalyzedEventListener(final SpeciesAnalysisRepository analysisRepository,
                                       final UUIDGenerator<SpecieAnalysis> uuidGenerator) {
        this.analysisRepository = analysisRepository;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public void onEvent(final SpecieAnalyzedEvent specieAnalyzedEvent) {
        final var speciesAnalysis = specieAnalyzedEvent.getSource();
        this.analysisRepository.save(speciesAnalysis.withUUID(uuidGenerator));
    }
}
