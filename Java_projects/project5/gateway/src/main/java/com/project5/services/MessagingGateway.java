
package com.project5.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.project5.config.RabbitMQConfig.OWNER_EXCHANGE;
import static com.project5.config.RabbitMQConfig.CAT_EXCHANGE;

@Service
public class MessagingGateway {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public MessagingGateway(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public <T, R> CompletableFuture<R> sendAndReceive(String exchange, String routingKey, T payload, TypeReference<R> responseTypeRef) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Object rawResponse = rabbitTemplate.convertSendAndReceive(exchange, routingKey, payload, message -> {
                    return message;
                });

                if (rawResponse == null) {
                    throw new RuntimeException("No response received from RabbitMQ within the configured timeout for routingKey: " + routingKey);
                }

                if (rawResponse instanceof String) {
                    return objectMapper.readValue((String) rawResponse, responseTypeRef); 
                } else {
                    
                    
                    return objectMapper.convertValue(rawResponse, responseTypeRef); 
                }

            } catch (Exception e) {
                throw new RuntimeException("Error during RabbitMQ RPC call for routingKey " + routingKey + ": " + e.getMessage(), e);
            }
        });
    }

    public <T, R> CompletableFuture<R> sendAndReceive(String exchange, String routingKey, T payload, Class<R> responseType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Object rawResponse = rabbitTemplate.convertSendAndReceive(exchange, routingKey, payload, message -> {
                    return message;
                });

                if (rawResponse == null) {
                    throw new RuntimeException("No response received from RabbitMQ within the configured timeout for routingKey: " + routingKey);
                }

                if (rawResponse instanceof String) {
                    return objectMapper.readValue((String) rawResponse, responseType);
                } else {
                    return objectMapper.convertValue(rawResponse, responseType);
                }

            } catch (Exception e) {
                throw new RuntimeException("Error during RabbitMQ RPC call for routingKey " + routingKey + ": " + e.getMessage(), e);
            }
        });
    }

    public <T, R> CompletableFuture<R> sendOwnerRequest(String routingKey, T payload, Class<R> responseType) {
        return sendAndReceive(OWNER_EXCHANGE, routingKey, payload, responseType);
    }

    public <T, R> CompletableFuture<R> sendOwnerRequest(String routingKey, T payload, TypeReference<R> responseTypeRef) {
        return sendAndReceive(OWNER_EXCHANGE, routingKey, payload, responseTypeRef);
    }

    public <T, R> CompletableFuture<R> sendCatRequest(String routingKey, T payload, Class<R> responseType) {
        return sendAndReceive(CAT_EXCHANGE, routingKey, payload, responseType);
    }

    
    public <T, R> CompletableFuture<R> sendCatRequest(String routingKey, T payload, TypeReference<R> responseTypeRef) {
        return sendAndReceive(CAT_EXCHANGE, routingKey, payload, responseTypeRef);
    }
}