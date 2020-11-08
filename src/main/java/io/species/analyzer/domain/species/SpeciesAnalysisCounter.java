package io.species.analyzer.domain.species;

import io.species.analyzer.infrastructure.generator.UUIDGenerator;
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

    @Column(name = "SPECIE")
    @Enumerated(EnumType.STRING)
    private final Specie specie;

    @Column(name = "COUNTER")
    private final Long counter;

    private SpeciesAnalysisCounter() {
        this(null, null, null);
    }

    private SpeciesAnalysisCounter(final UUID uuid, final Specie specie, final Long counter) {
        this.uuid = uuid;
        this.specie = specie;
        this.counter = counter;
    }

    private SpeciesAnalysisCounter(final UUID uuid, final Specie identifier) {
        this(uuid, identifier, 1L);
    }

    public static SpeciesAnalysisCounter of(final Specie specie) {
        return new SpeciesAnalysisCounter(null, specie);
    }

    public SpeciesAnalysisCounter withUUID(final UUID uuid) {
        return new SpeciesAnalysisCounter(uuid, specie, counter);
    }

    public SpeciesAnalysisCounter withUUID(final UUIDGenerator<SpeciesAnalysisCounter> uuidGenerator) {
        return this.withUUID(uuidGenerator.generate(this));
    }

    @NonNull
    public Specie getSpecie() {
        return specie;
    }

    public Long getCounter() {
        return counter;
    }
}
