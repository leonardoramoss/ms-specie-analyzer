package io.species.analyzer.domain.species;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(schema = "SPECIE", name = "SPECIES_ANALYSIS_COUNTER")
public class SpeciesAnalysisCounter {

    @Id
    @Column(name = "UUID")
    private final UUID uuid;

    @NonNull
    @Column(name = "SPECIE")
    @Enumerated(EnumType.STRING)
    private final SpeciesIdentifier identifier;

    @Column(name = "COUNTER")
    private Long counter;

    private SpeciesAnalysisCounter() {
        this(null, null, null);
    }

    private SpeciesAnalysisCounter(final UUID uuid, final SpeciesIdentifier identifier, final Long counter) {
        this.uuid = uuid;
        this.identifier = identifier;
        this.counter = counter;
    }

    private SpeciesAnalysisCounter(final UUID uuid, final SpeciesIdentifier identifier) {
        this(uuid, identifier, 0L);
    }

    public static SpeciesAnalysisCounter of(final SpeciesIdentifier identifier) {
        return new SpeciesAnalysisCounter(null, identifier);
    }

    public SpeciesAnalysisCounter withUUID(final UUID uuid) {
        return new SpeciesAnalysisCounter(uuid, identifier, counter);
    }

    public SpeciesIdentifier getIdentifier() {
        return identifier;
    }

    public Long getCounter() {
        return counter;
    }

    public SpeciesAnalysisCounter increment() {
        this.counter = this.counter + 1;
        return this;
    }
}
