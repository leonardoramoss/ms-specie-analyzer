package io.species.analyzer.infrastructure.converter.attribute;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDAttributeConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(final UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID convertToEntityAttribute(final String s) {
        return UUID.fromString(s);
    }
}
