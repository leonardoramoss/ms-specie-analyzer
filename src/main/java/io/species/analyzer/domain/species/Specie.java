package io.species.analyzer.domain.species;

import io.species.analyzer.infrastructure.exception.SpecieValidationException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(schema = "DNA", name = "SPECIE")
public class Specie {

    private static final Pattern ALLOWED_NITROGENOUS_BASE = Pattern.compile("[ATCG]+");
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
        if(dna != null && dna.length > 0) {
            checkAllowedNitrogenousBase(dna);
            return new Specie(null, String.join(DELIMITER, dna));
        }
        throw new IllegalArgumentException();
    }

    private static void checkAllowedNitrogenousBase(final String[] dnaChain) {
        Arrays.stream(dnaChain).forEach(dna -> {
            final Matcher matcher = ALLOWED_NITROGENOUS_BASE.matcher(dna);
            if(!matcher.matches()) {
                throw new SpecieValidationException(String.format("DNA sequence %s in %s is not valid.", dna, Arrays.toString(dnaChain)));
            }
        });
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
