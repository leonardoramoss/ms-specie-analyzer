package io.species.analyzer.domain.species;

import io.species.analyzer.domain.event.SpecieAnalyzedEvent;
import io.species.analyzer.domain.species.analyzer.Analyzer;
import io.species.analyzer.infrastructure.exception.SpecieAnalysisNotMatchesException;
import io.species.analyzer.infrastructure.exception.SpecieValidationException;
import io.species.analyzer.infrastructure.generator.UUIDGenerator;

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
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNullElse;

@Entity
@Table(schema = "SPECIE", name = "SPECIES_ANALYSIS")
public class SpecieAnalysis {

    private static final Pattern ALLOWED_NITROGENOUS_BASE = Pattern.compile("[ATCG]+");
    private static final String DELIMITER = "-";
    private static final int MINIMUM_NxN_LENGTH = 4;

    @Id
    @Column(name = "UUID")
    private final UUID uuid;

    @Column(name = "DNA")
    private final String dna;

    @Column(name = "SPECIE")
    @Enumerated(EnumType.STRING)
    private Specie specie;

    @Column(name = "ANALYZED_AT")
    private LocalDateTime analyzedAt;

    @Transient
    private Specie expectedSpecie;

    private SpecieAnalysis() {
        this(null, null, null);
    }

    private SpecieAnalysis(final UUID uuid, final String dna) {
        this(uuid, dna, Specie.NOT_IDENTIFIED);
        this.expectedSpecie = Specie.NOT_IDENTIFIED;
    }

    private SpecieAnalysis(final UUID uuid, final String dna, final Specie specie) {
        this.uuid = uuid;
        this.dna = dna;
        this.specie = specie;
        this.expectedSpecie = specie;
        this.analyzedAt = LocalDateTime.now();
    }

    public static SpecieAnalysis of(final String[] dna) {
        if(dna != null && dna.length >= MINIMUM_NxN_LENGTH) {
            checkAllowedNitrogenousBase(dna);
            checkDNAStructure(dna);
            return new SpecieAnalysis(null, String.join(DELIMITER, dna));
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

    private static void checkDNAStructure(final String[] dna) {
        final var sequenceLength = dna.length;
        Arrays.stream(dna).forEach(sequence -> {
            if(sequenceLength != sequence.length()) {
                throw new SpecieValidationException("There is not a NxN DNA sequence.");
            }
        });
    }

    public UUID getUuid() {
        return uuid;
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
     * @return
     */
    public Specie getSpecie() {
        return specie;
    }

    /**
     *
     * @return
     */
    public Specie getExpectedSpecie() {
        return requireNonNullElse(expectedSpecie, Specie.NOT_IDENTIFIED);
    }

    /**
     *
     * @param uuidGenerator
     * @return
     */
    public SpecieAnalysis withUUID(final UUIDGenerator<SpecieAnalysis> uuidGenerator) {
        return this.withUUID(uuidGenerator.generate(this));
    }

    /**
     *
     * @param uuid
     * @return
     */
    public SpecieAnalysis withUUID(final UUID uuid) {
        return new SpecieAnalysis(uuid, dna, specie);
    }


    /**
     *
     * @param analyzer
     * @return
     */
    public SpecieAnalysis with(final Analyzer analyzer) {
        return with(analyzer, null);
    }

    /**
     *
     * @param analyzer
     * @return
     */
    public SpecieAnalysis with(final Analyzer analyzer, final Consumer<SpecieAnalyzedEvent> callback) {
        final var optionalSpecie = analyzer.analyze(this);

        this.specieAs(optionalSpecie.orElse(Specie.HUMAN));

        Optional.ofNullable(callback)
                .ifPresent(consumer -> consumer.accept(SpecieAnalyzedEvent.of(this)));

        if(!this.isMatchesAsExpected()) {
            throw new SpecieAnalysisNotMatchesException(String.format("Expected: %s, Found: %s", expectedSpecie, specie));
        }

        return this;
    }

    /**
     *
     * @param specie
     * @return
     */
    private void specieAs(final Specie specie) {
        Objects.requireNonNull(specie);
        this.specie = specie;
        this.analyzedAt = LocalDateTime.now();
    }

    /**
     *
     * @param expectedIdentifier
     * @return
     */
    public SpecieAnalysis expectedSpecieAs(final Specie expectedIdentifier) {
        Objects.requireNonNull(expectedIdentifier);
        this.expectedSpecie = expectedIdentifier;
        return this;
    }

    /**
     *
     * @param expectedIdentifier
     * @return
     */
    public SpecieAnalysis expectedSpecieAs(final String expectedIdentifier) {
        Objects.requireNonNull(expectedIdentifier);
        return this.expectedSpecieAs(Specie.byName(expectedIdentifier));
    }

    /**
     *
     * @return
     */
    private boolean isMatchesAsExpected() {
        return Optional.ofNullable(this.specie)
                .orElseThrow(IllegalArgumentException::new)
                .equals(this.expectedSpecie);
    }
}
