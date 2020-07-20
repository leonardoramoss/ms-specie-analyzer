package io.species.analyzer.infrastructure.serialization.serializer;

import io.species.analyzer.domain.species.stats.StatsHumanSimianRatioResult;
import io.species.analyzer.domain.species.stats.StatsResult;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;
import io.species.analyzer.presentation.wrapper.StatsResultWrapper;

import java.io.IOException;
import java.util.Optional;

public class StatsResultWrapperSerializer extends AbstractSerializer<StatsResultWrapper> {

    @Override
    public void serialize(final StatsResultWrapper statsResultWrapper, final SerializationLabel serializationLabel, final JsonWriter jsonWriter) throws IOException {
        final Optional<StatsResult> resultOptional = statsResultWrapper.getStats();
        if(resultOptional.isPresent()) {
            final var result = (StatsHumanSimianRatioResult) resultOptional.get();
            jsonWriter.writeStartObject(serializationLabel);
            jsonWriter.writeNumberField(StatsHumanSimianRatioLabel.SIMIAN, result.getSimianCount());
            jsonWriter.writeNumberField(StatsHumanSimianRatioLabel.HUMAN, result.getHumanCount());
            jsonWriter.writeNumberField(StatsHumanSimianRatioLabel.RATIO, result.result());
            jsonWriter.writeEndObject();
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
