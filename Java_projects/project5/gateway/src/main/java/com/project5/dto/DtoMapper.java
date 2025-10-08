package com.project5.dto; 

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ObjectMapper objectMapper;

    public DtoMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CatDto toCatDto(String json) {
        try {
            return objectMapper.readValue(json, CatDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize CatDto from JSON", e);
        }
    }

    public OwnerDto toOwnerDto(String json) {
        try {
            return objectMapper.readValue(json, OwnerDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize OwnerDto from JSON", e);
        }
    }

    
    
}