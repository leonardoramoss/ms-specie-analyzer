package io.species.analyzer.domain.species;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpeciesAnalysisRepository extends JpaRepository<SpeciesAnalysis, UUID> {

    @Query(value = "SELECT s.identifier, COUNT(s.uuid) FROM SpeciesAnalysis s WHERE s.identifier IN ?1 GROUP BY s.identifier")
    List<Object[]> countSpeciesAnalysesByIdentifierIn(final List<SpeciesIdentifier> identifiers);
}
