package io.species.analyzer.domain.species;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface SpecieAnalysisCounterRepository extends JpaRepository<SpeciesAnalysisCounter, UUID> {

    List<SpeciesAnalysisCounter> findAllByIdentifierIn(final List<SpeciesIdentifier> identifiers);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SpeciesAnalysisCounter s SET s.counter = s.counter + 1 WHERE s.identifier = ?1")
    int incrementSpecieCounter(final SpeciesIdentifier speciesIdentifier);
}
