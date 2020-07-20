package io.species.analyzer.configuration;

import io.species.analyzer.infrastructure.serialization.serializer.AbstractSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class DatabindConfiguration {

    @Bean
    public Map<Class<?>, AbstractSerializer> serializers(final List<? extends AbstractSerializer> serializers) {
        return serializers.stream().collect(Collectors.toMap(AbstractSerializer::type, Function.identity()));
    }
}
