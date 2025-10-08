package com.project5.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project5.config.RabbitMQConfig;
import com.project5.dto.OwnerDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class OwnerServiceProxy {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OwnerServiceProxy(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    
    public <T, R> CompletableFuture<R> sendAndReceive(String routingKey, T payload, Class<R> responseType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                
                Object rawResponse = rabbitTemplate.convertSendAndReceive(RabbitMQConfig.OWNER_EXCHANGE, routingKey, payload);

                if (rawResponse == null) {
                    throw new RuntimeException("No response received from Owner Service for routingKey: " + routingKey);
                }

                if (rawResponse instanceof String) {
                    return objectMapper.readValue((String) rawResponse, responseType);
                } else {
                    return objectMapper.convertValue(rawResponse, responseType);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error during RPC call to Owner Service for routingKey " + routingKey + ": " + e.getMessage(), e);
            }
        });
    }

    
    public CompletableFuture<OwnerDto> getOwnerById(Long ownerId) {
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("id", ownerId); 

        return sendAndReceive(RabbitMQConfig.OWNER_GET_BY_ID_KEY, requestPayload, OwnerDto.class);
    }
}