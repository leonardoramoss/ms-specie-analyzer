package io.species.analyzer.infrastructure.generator;

import io.species.analyzer.domain.species.Specie;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpecieUUIDGenerator implements UUIDGenerator<Specie> {

    @Override
    public UUID generate(final Specie specie) {
        return UUID.nameUUIDFromBytes(specie.getDna().getBytes());
    }
}
