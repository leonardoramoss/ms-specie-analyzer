package io.species.analyzer.domain.species;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(schema = "SPECIE", name = "DNA")
public class Specie {

    private static final String DELIMITER = "-";

    @Id
    @Column(name = "UUID")
    private final UUID uuid;

    @Column(name = "DNA")
    private final String dna;

    @Column(name = "SPECIE")
    @Enumerated(EnumType.STRING)
    private Species type;

    @Transient
    private Species candidate;

    private Specie() {
        this(null, null);
    }

    private Specie(final UUID uuid, final String dna) {
        this.uuid = uuid;
        this.dna = dna;
        this.type = Species.NOT_IDENTIFIED;
        this.candidate = Species.NOT_IDENTIFIED;
    }

    private Specie(final UUID uuid, final String dna, final Species species) {
        this.uuid = uuid;
        this.dna = dna;
        this.type = species;
    }

    public static Specie of(final String[] dna) {
        final String dnaSequence = String.join(DELIMITER, Optional.ofNullable(dna).orElseThrow(IllegalArgumentException::new));
        return new Specie(null, dnaSequence);
    }

    /**
     *
     * @return
     */
    public String[] getOriginalDna() {
        return this.dna.split(DELIMITER);
    }

    /**
     *
     * @return
     */
    public String getDna() {
        return this.dna;
    }
    /**
     *
     * @param uuid
     * @return
     */
    public Specie withUUID(final UUID uuid) {
        return new Specie(uuid, dna, type);
    }

    /**
     *
     * @return
     */
    public Species getCandidate() {
        return candidate;
    }

    /**
     *
     * @param species
     * @return
     */
    public Specie markAs(final Species species) {
        this.type = species;
        return this;
    }

    /**
     *
     * @param species
     * @return
     */
    public Specie markCandidateFor(final Species species) {
        this.candidate = species;
        return this;
    }

    /**
     *
     * @param species
     * @return
     */
    public boolean isSpeciesOf(final Species species) {
        return Optional.ofNullable(this.type)
                .orElseThrow(IllegalArgumentException::new)
                .equals(species);
    }
}
