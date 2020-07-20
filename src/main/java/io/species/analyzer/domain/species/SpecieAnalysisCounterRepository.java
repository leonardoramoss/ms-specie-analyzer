package io.species.analyzer.domain.species;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpecieAnalysisCounterRepository extends JpaRepository<SpeciesAnalysisCounter, UUID> {

    List<SpeciesAnalysisCounter> findAllByIdentifierIn(final List<SpeciesIdentifier> identifiers);
}
