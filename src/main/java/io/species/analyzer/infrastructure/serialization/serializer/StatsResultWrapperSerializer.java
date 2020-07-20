package io.species.analyzer.infrastructure.serialization.serializer;

import io.species.analyzer.domain.species.stats.StatsHumanSimianRatioResult;
import io.species.analyzer.domain.species.stats.StatsResult;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;
import io.species.analyzer.presentation.wrapper.StatsResultWrapper;

import java.io.IOException;
import java.util.Optional;

public class StatsResultWrapperSerializer extends AbstractSerializer<StatsResultWrapper> {

    @Override
    public void serialize(final StatsResultWrapper statsResultWrapper) throws IOException {
        final Optional<StatsResult> resultOptional = statsResultWrapper.getStats();
        if(resultOptional.isPresent()) {
            final var result = (StatsHumanSimianRatioResult) resultOptional.get();
            writeStartObject();
            writeNumberField(StatsHumanSimianRatioLabel.SIMIAN, result.getSimianCount());
            writeNumberField(StatsHumanSimianRatioLabel.HUMAN, result.getHumanCount());
            writeNumberField(StatsHumanSimianRatioLabel.RATIO, result.result());
            writeEndObject();
        }
    }

    private enum StatsHumanSimianRatioLabel implements SerializationLabel {

        HUMAN("count_human_dna"),
        SIMIAN("count_mutant_dna"),
        RATIO("ratio");

        private final String label;

        StatsHumanSimianRatioLabel(final String label) {
            this.label = label;
        }

        @Override
        public String label() {
            return label;
        }
    }
}
