package io.species.analyzer.infrastructure.serialization.serializer;

import io.species.analyzer.infrastructure.context.DefaultApplicationContextHolder;
import io.species.analyzer.presentation.wrapper.StatsResultWrapper;

import java.io.IOException;
import java.util.Map;

public class StatsResultWrapperSerializer extends AbstractSerializer<StatsResultWrapper> {

    final Map<Class, AbstractSerializer> serializers = (Map<Class, AbstractSerializer>) DefaultApplicationContextHolder.getBean("serializers");

    @Override
    public void serialize(final StatsResultWrapper statsResultWrapper, final JsonWriter jsonWriter) throws IOException {
        final var optionalStatsResult = statsResultWrapper.getStats();
        if(optionalStatsResult.isPresent()) {
            final var statsResult = optionalStatsResult.get();
            final var serializer = serializers.get(statsResult.getClass());
            serializer.serialize(statsResult, jsonWriter);
        }
    }
}
