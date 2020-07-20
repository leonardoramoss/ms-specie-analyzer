package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;
import io.species.analyzer.presentation.wrapper.SpeciesAnalysisWrapper;
import org.springframework.stereotype.Component;

@Component
public class SpeciesAnalysisWrapperDeserializer extends AbstractDeserializer<SpeciesAnalysisWrapper> {

    @Override
    public SpeciesAnalysisWrapper deserialize(final JsonNode jsonNode) {
        final var dnaChain = readFieldAs(jsonNode, SpecieLabels.DNA, String[].class);
        return new SpeciesAnalysisWrapper(SpeciesAnalysis.of(dnaChain));
    }

    private enum SpecieLabels implements SerializationLabel {

        DNA("dna");

        private final String label;

        SpecieLabels(final String label) {
            this.label = label;
        }

        @Override
        public String label() {
            return label;
        }
    }
}
