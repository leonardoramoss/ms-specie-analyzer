package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import io.species.analyzer.domain.species.SpeciesAnalysis;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;
import io.species.analyzer.presentation.wrapper.SpecieWrapper;
import org.springframework.stereotype.Component;

@Component
public class SpecieDeserializer extends AbstractDeserializer<SpecieWrapper> {

    @Override
    public SpecieWrapper deserialize(final JsonNode jsonNode) {
        final String[] dnaChain = readFieldAs(jsonNode, SpecieLabels.DNA, String[].class);
        return new SpecieWrapper(SpeciesAnalysis.of(dnaChain));
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
