package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.presentation.wrapper.SpecieWrapper;

public class SpecieDeserializer extends AbstractDeserializer<SpecieWrapper> {

    @Override
    public SpecieWrapper deserialize(final JsonNode jsonNode) {
        final String[] dnaChain = extractAs(jsonNode, SpecieLabels.DNA, String[].class);

        return new SpecieWrapper(Specie.of(dnaChain));
    }

    private enum SpecieLabels {

        DNA("dna");

        private final String label;

        SpecieLabels(final String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }
}
