package com.epam.esm.converter;

import com.epam.esm.model.DTO.tag.CreateTagRequest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import javax.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateTagRequestConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object object, Class<?> targetType) throws ArgumentConversionException {

        assertEquals(CreateTagRequest.class, targetType, "It will only convert to CreateTagRequest");

        CreateTagRequest createTagRequest = CreateTagRequest.builder()
                .name(((JsonObject) object).getString("name"))
                .build();

        return createTagRequest;
    }
}
