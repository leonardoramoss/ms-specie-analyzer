package io.species.analyzer.domain.event;

import io.species.analyzer.domain.species.SpeciesAnalysis;

public class SpecieAnalyzedEvent extends DomainEvent<SpeciesAnalysis> {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    private SpecieAnalyzedEvent(final SpeciesAnalysis source) {
        super(source);
    }

    @Override
    public EventType eventType() {
        return EventType.SPECIE_ANALYZED;
    }

    public static SpecieAnalyzedEvent of(final SpeciesAnalysis speciesAnalysis) {
        return new SpecieAnalyzedEvent(speciesAnalysis);
    }
}
