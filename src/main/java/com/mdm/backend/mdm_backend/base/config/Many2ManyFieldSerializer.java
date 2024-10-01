package com.mdm.backend.mdm_backend.base.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

public class Many2ManyFieldSerializer<T> extends JsonSerializer<Set<T>> {

    @Override
    public void serialize(Set<T> items, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartArray();
        for (T item : items) {
            gen.writeStartObject();
            try {
                // Fetch the "id" field

                String idValue = getFieldValue(item, "id");
                if (idValue != null) {
                    gen.writeStringField("id", idValue);
                }

                // Try to fetch "name" or fall back to "username"
                String nameValue = getFieldValue(item, "name");
                if (nameValue == null || nameValue.isEmpty()) {
                    nameValue = getFieldValue(item, "username");
                }

                // If either field is found, add it to the JSON output
                if (nameValue != null) {
                    gen.writeStringField("name", nameValue);
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle exception, could log this
            }
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

    // Helper method to get a field value dynamically
    private String getFieldValue(Object value, String fieldName) {
        try {
            Field field = value.getClass().getDeclaredField(fieldName);
            if (field ==null){
                field = value.getClass().getSuperclass().getDeclaredField(fieldName);
            }
            field.setAccessible(true);
            Object fieldValue = field.get(value);
            return fieldValue != null ? fieldValue.toString() : null;
        } catch (NoSuchFieldException e) {
            // If not found in superclass, try in the current class
            try {
                Field field = value.getClass().getSuperclass().getDeclaredField(fieldName);
                if (field ==null){
                    field = value.getClass().getDeclaredField(fieldName);
                }
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

