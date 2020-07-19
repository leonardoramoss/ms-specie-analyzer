package io.species.analyzer.domain.species;

import io.species.analyzer.infrastructure.exception.SpecieValidationException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(schema = "SPECIE", name = "SPECIES_ANALYSIS")
public class SpeciesAnalysis {

    private static final Pattern ALLOWED_NITROGENOUS_BASE = Pattern.compile("[ATCG]+");
    private static final String DELIMITER = "-";

    @Id
    @Column(name = "UUID")
    private final UUID uuid;

    @Column(name = "DNA")
    private final String dna;

    @Column(name = "SPECIE")
    @Enumerated(EnumType.STRING)
    private SpeciesIdentifier identifier;

    @Column(name = "ANALYZED_AT")
    private LocalDateTime analyzedAt;

    @Transient
    private SpeciesIdentifier expectedIdentifier;

    private SpeciesAnalysis() {
        this(null, null, null);
    }

    private SpeciesAnalysis(final UUID uuid, final String dna) {
        this(uuid, dna, SpeciesIdentifier.NOT_IDENTIFIED);
        this.expectedIdentifier = SpeciesIdentifier.NOT_IDENTIFIED;
    }

    private SpeciesAnalysis(final UUID uuid, final String dna, final SpeciesIdentifier specie) {
        this.uuid = uuid;
        this.dna = dna;
        this.identifier = specie;
        this.expectedIdentifier = specie;
        this.analyzedAt = LocalDateTime.now();
    }

    public static SpeciesAnalysis of(final String[] dna) {
        if(dna != null && dna.length > 0) {
            checkAllowedNitrogenousBase(dna);
            return new SpeciesAnalysis(null, String.join(DELIMITER, dna));
        }
        throw new SpecieValidationException(String.format("DNA sequence %s is invalid.", Arrays.toString(dna)));
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
    public SpeciesAnalysis withUUID(final UUID uuid) {
        return new SpeciesAnalysis(uuid, dna, identifier);
    }

    /**
     *
     * @return
     */
    public SpeciesIdentifier getExpectedIdentifier() {
        return expectedIdentifier;
    }

    public SpeciesIdentifier getIdentifier() {
        return identifier;
    }

    /**
     *
     * @param speciesIdentifier
     * @return
     */
    public SpeciesAnalysis markIdentifiedAs(final SpeciesIdentifier speciesIdentifier) {
        Objects.requireNonNull(speciesIdentifier);
        this.identifier = speciesIdentifier;
        return this;
    }

    /**
     *
     * @param expectedIdentifier
     * @return
     */
    public SpeciesAnalysis markExpectedIdentifierAs(final SpeciesIdentifier expectedIdentifier) {
        Objects.requireNonNull(expectedIdentifier);
        this.expectedIdentifier = expectedIdentifier;
        return this;
    }

    /**
     *
     * @return
     */
    public boolean isIdentifierMatchesAsExpected() {
        if(Objects.isNull(this.expectedIdentifier)) {
            return true;
        }

        return Optional.ofNullable(this.identifier)
                .orElseThrow(IllegalArgumentException::new)
                .equals(this.expectedIdentifier);
    }
}
