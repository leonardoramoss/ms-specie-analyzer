package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import io.species.analyzer.domain.species.SpecieAnalysis;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;
import io.species.analyzer.presentation.wrapper.SpecieAnalysisWrapper;
import org.springframework.stereotype.Component;

@Component
public class SpeciesAnalysisWrapperDeserializer extends AbstractDeserializer<SpecieAnalysisWrapper> {

    @Override
    public SpecieAnalysisWrapper deserialize(final JsonNode jsonNode) {
        final var dnaChain = readFieldAs(jsonNode, SpecieLabels.DNA, String[].class);
        return new SpecieAnalysisWrapper(SpecieAnalysis.of(dnaChain));
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
