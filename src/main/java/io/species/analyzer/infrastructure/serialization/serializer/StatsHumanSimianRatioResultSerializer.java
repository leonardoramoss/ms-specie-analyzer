package io.species.analyzer.infrastructure.serialization.serializer;

import io.species.analyzer.domain.species.stats.StatsHumanSimianRatioResult;
import io.species.analyzer.infrastructure.serialization.SerializationLabel;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StatsHumanSimianRatioResultSerializer extends AbstractSerializer<StatsHumanSimianRatioResult> {

    @Override
    public void serialize(final StatsHumanSimianRatioResult statsResult, final JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeNumberField(StatsHumanSimianRatioLabel.SIMIAN, statsResult.getSimianCount());
        jsonWriter.writeNumberField(StatsHumanSimianRatioLabel.HUMAN, statsResult.getHumanCount());
        jsonWriter.writeNumberField(StatsHumanSimianRatioLabel.RATIO, statsResult.result());
        jsonWriter.writeEndObject();
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
