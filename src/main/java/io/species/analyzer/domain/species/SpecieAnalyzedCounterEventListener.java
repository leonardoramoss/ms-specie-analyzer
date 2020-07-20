package io.species.analyzer.domain.species;

import io.species.analyzer.domain.event.EventListener;
import io.species.analyzer.domain.event.SpecieAnalyzedEvent;
import io.species.analyzer.infrastructure.command.Command;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class SpecieAnalyzedCounterEventListener implements EventListener<SpecieAnalyzedEvent> {

    private final Command<SpeciesAnalysis> incrementSpecieAnalyzedCounterCommand;
    private final ExecutorService executorService;

    public SpecieAnalyzedCounterEventListener(final Command<SpeciesAnalysis> incrementSpecieAnalyzedCounterCommand,
                                              final ExecutorService executorService) {
        this.incrementSpecieAnalyzedCounterCommand = incrementSpecieAnalyzedCounterCommand;
        this.executorService = executorService;
    }

    @Override
    public void onEvent(final SpecieAnalyzedEvent event) {
        executorService.execute(() -> incrementSpecieAnalyzedCounterCommand.execute(event.getSource()));
    }
}
