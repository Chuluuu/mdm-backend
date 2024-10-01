package com.mdm.backend.mdm_backend.base.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Field;

public class Many2OneFieldSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();

        // Use reflection to extract the "id" field
        try {
            Field idField = value.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(value);
            gen.writeObjectField("id", idValue);

            String nameValue = getFieldValue(value, "name");
            if (nameValue == null || nameValue.isEmpty()) {
                nameValue = getFieldValue(value, "username");
            }
            // Add the name or username field only if it's not empty or null
            if (nameValue != null && !nameValue.isEmpty()) {
                gen.writeStringField("name", nameValue);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IOException("Failed to serialize ManyToOne field", e);
        }

        gen.writeEndObject();
    }
    private String getFieldValue(Object value, String fieldName) {
        try {
            Field field = value.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldValue = field.get(value);
            return fieldValue != null ? fieldValue.toString() : null;
        } catch (NoSuchFieldException e) {
            // If not found in superclass, try in the current class
            try {
                Field field = value.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object fieldValue = field.get(value);
                return fieldValue != null ? fieldValue.toString() : null;
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                return null; // Return null if field doesn't exist
            }
        } catch (IllegalAccessException e) {
            return null; // Handle illegal access exception
        }
    }

}
