package com.epam.esm.converter;

import com.epam.esm.model.entity.Tag;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import javax.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object object, Class<?> targetType) throws ArgumentConversionException {
        assertEquals(Tag.class, targetType, "It will only convert to Tag");

        return  Tag.builder()
                .id(Long.parseLong(((JsonObject) object).getJsonNumber("id").toString()))
                .name(((JsonObject) object).getString("name"))
                .build();
    }
}
