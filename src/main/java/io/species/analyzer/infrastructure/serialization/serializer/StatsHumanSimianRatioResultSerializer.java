package io.species.analyzer.infrastructure.serialization.serializer;

import io.species.analyzer.domain.species.stats.StatsHumanSimianRatioResult;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;

import java.io.IOException;

public class StatsHumanSimianRatioResultSerializer extends AbstractSerializer<StatsHumanSimianRatioResult> {

    @Override
    public void serialize(final StatsHumanSimianRatioResult ratioResult) throws IOException {
        writeStartObject();
        writeNumberField(StatsHumanSimianRatioLabel.SIMIAN, ratioResult.getSimianCount());
        writeNumberField(StatsHumanSimianRatioLabel.HUMAN, ratioResult.getHumanCount());
        writeNumberField(StatsHumanSimianRatioLabel.RATIO, ratioResult.result());
        writeEndObject();
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
