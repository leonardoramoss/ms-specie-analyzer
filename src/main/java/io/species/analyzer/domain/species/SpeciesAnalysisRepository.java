package io.species.analyzer.domain.species;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpeciesAnalysisRepository extends JpaRepository<SpeciesAnalysis, UUID> {
}
