package io.species.analyzer.domain.species;

public enum SpeciesIdentifier {

    SIMIAN("SIMIAN"),
    HUMAN("HUMAN"),
    NOT_IDENTIFIED("NOT_IDENTIFIED");

    private final String identifier;

    SpeciesIdentifier (final String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return this.identifier;
    }

}
