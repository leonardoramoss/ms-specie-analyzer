package io.species.analyzer.infrastructure.serialization.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.infrastructure.exception.serialization.SpecieDeserializationException;
import io.species.analyzer.presentation.wrapper.SpecieWrapper;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecieDeserializer extends AbstractDeserializer<SpecieWrapper> {

    static final Pattern ALLOWED_NITROGENOUS_BASE = Pattern.compile("[ATCG]+");

    @Override
    public SpecieWrapper deserialize(final JsonNode jsonNode) {
        final String[] dnaChain = extractAs(jsonNode, SpecieLabels.DNA, String[].class);
        checkAllowedNitrogenousBase(dnaChain);
        return new SpecieWrapper(Specie.of(dnaChain));
    }

    private void checkAllowedNitrogenousBase(final String[] dnaChain) {
        Arrays.asList(dnaChain).forEach(dna -> {
            final Matcher matcher = ALLOWED_NITROGENOUS_BASE.matcher(dna);
            if(!matcher.matches()) {
                throw new SpecieDeserializationException("");
            }
        });
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
