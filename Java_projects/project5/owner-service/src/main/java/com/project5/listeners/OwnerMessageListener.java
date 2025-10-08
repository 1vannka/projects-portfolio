package com.project5.listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project5.config.RabbitMQConfig; 
import com.project5.dto.OwnerDto;
import com.project5.models.Owners;
import com.project5.services.OwnerService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OwnerMessageListener {

    private final OwnerService ownerService;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OwnerMessageListener(OwnerService ownerService, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.ownerService = ownerService;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    
    private void sendReply(String replyTo, String correlationId, Object responsePayload, HttpStatus responseStatus, String currentRoutingKey) {
        if (replyTo != null && correlationId != null) {
            System.out.println("  Sending reply for " + currentRoutingKey + " to: " + replyTo + " with CorrelationId: " + correlationId +
                    ", Status: " + responseStatus.value());
            rabbitTemplate.convertAndSend(replyTo, responsePayload, m -> {
                m.getMessageProperties().setCorrelationId(correlationId);
                m.getMessageProperties().setHeader("X-HTTP-Status", responseStatus.value());
                return m;
            });
        } else {
            System.err.println("  Warning: No replyTo or correlationId present for routingKey: " + currentRoutingKey + ". Reply cannot be sent.");
        }
    }

    
    private Map<String, Object> createErrorMap(HttpStatus status, String message) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("error", message);
        errorMap.put("status", status.value());
        return errorMap;
    }


    

    @RabbitListener(queues = "${owner.queue.create}")
    public void handleOwnerCreateRequest(Message message) {
        String routingKey = RabbitMQConfig.OWNER_CREATE_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("OwnerMessageListener: Получен запрос на создание владельца. CorrelationId: " + correlationId);

        try {
            
            Map<String, Object> createRequestData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Обработка OWNER_CREATE_KEY. Полученные данные: " + createRequestData);

            OwnerDto ownerToCreate = objectMapper.convertValue(createRequestData, OwnerDto.class);

            Owners newOwner = ownerService.createOwner(ownerToCreate); 

            responsePayload = ownerService.toOwnerDto(newOwner);
            responseStatus = HttpStatus.CREATED;
            System.out.println("  Владелец создан: " + newOwner.getName() + " с ID: " + newOwner.getId());

        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException при создании: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Внутренняя ошибка сервера: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  Общее исключение при создании: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${owner.queue.byId}")
    public void handleOwnerGetByIdRequest(Message message) {
        String routingKey = RabbitMQConfig.OWNER_GET_BY_ID_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("OwnerMessageListener: Received request on getById queue. CorrelationId: " + correlationId);

        try {
            
            Map<String, Object> getIdData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            Long ownerId = ((Number) getIdData.get("id")).longValue();
            System.out.println("  Processing OWNER_GET_BY_ID_KEY. ID: " + ownerId);

            Owners owner = ownerService.getOwnerById(ownerId);
            if (owner == null) {
                responseStatus = HttpStatus.NOT_FOUND;
                errorMessage = "Owner not found with id: " + ownerId;
                responsePayload = createErrorMap(responseStatus, errorMessage);
            } else {
                responsePayload = ownerService.toOwnerDto(owner);
                System.out.println("  Owner found: " + owner.getName());
            }
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for getById: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for getById: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${owner.queue.all}")
    public void handleOwnerGetAllRequest(Message message) {
        String routingKey = RabbitMQConfig.OWNER_GET_ALL_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("OwnerMessageListener: Received request on getAll queue. CorrelationId: " + correlationId);

        try {
            Map<String, Object> getAllParams = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing OWNER_GET_ALL_KEY. Params: " + getAllParams);

            int page = (Integer) getAllParams.getOrDefault("page", 0);
            int size = (Integer) getAllParams.getOrDefault("size", 10);
            String sortString = (String) getAllParams.getOrDefault("sort", "id:ASC");

            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            if (sortString != null && !sortString.isEmpty() && !sortString.equals("UNSORTED")) {
                String[] sortParts = sortString.split(":");
                if (sortParts.length == 2) {
                    try {
                        String property = sortParts[0].trim();
                        Sort.Direction direction = Sort.Direction.fromString(sortParts[1].trim().toUpperCase());
                        sort = Sort.by(direction, property);
                    } catch (IllegalArgumentException e) {
                        System.err.println("  Warning: Invalid sort string: " + sortString + ". Using default sort.");
                    }
                }
            }
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Owners> ownersPage;
            String ownerNameFilter = (String) getAllParams.get("name");
            if (ownerNameFilter != null && !ownerNameFilter.isEmpty()) {
                ownersPage = ownerService.getOwnersByNamePaginated(ownerNameFilter, pageable);
            } else {
                ownersPage = ownerService.findAllOwners(pageable);
            }

            Map<String, Object> pageResponse = new HashMap<>();
            pageResponse.put("content", ownersPage.getContent().stream().map(ownerService::toOwnerDto).collect(Collectors.toList()));
            pageResponse.put("number", ownersPage.getNumber());
            pageResponse.put("size", ownersPage.getSize());
            pageResponse.put("totalElements", ownersPage.getTotalElements());
            pageResponse.put("totalPages", ownersPage.getTotalPages());
            pageResponse.put("last", ownersPage.isLast());
            pageResponse.put("first", ownersPage.isFirst());
            pageResponse.put("empty", ownersPage.isEmpty());

            responsePayload = pageResponse;
            System.out.println("  Returned " + ownersPage.getTotalElements() + " owners.");
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for getAll: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for getAll: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${owner.queue.update}")
    public void handleOwnerUpdateRequest(Message message) {
        String routingKey = RabbitMQConfig.OWNER_UPDATE_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("OwnerMessageListener: Received request on update queue. CorrelationId: " + correlationId);

        try {
            Map<String, Object> updateData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing OWNER_UPDATE_KEY. Received data: " + updateData);

            Long updateId = ((Number) updateData.get("id")).longValue();
            OwnerDto ownerUpdateDto = objectMapper.convertValue(updateData.get("ownerDto"), OwnerDto.class);
            Long currentUserIdUpdate = updateData.get("currentUserId") != null ? ((Number) updateData.get("currentUserId")).longValue() : null;
            Set<String> currentUserRolesUpdate = objectMapper.convertValue(updateData.get("currentUserRoles"), new TypeReference<Set<String>>() {});

            Owners updatedOwner = ownerService.updateOwner(updateId, ownerUpdateDto, currentUserIdUpdate, currentUserRolesUpdate);
            responsePayload = ownerService.toOwnerDto(updatedOwner);
            System.out.println("  Owner updated: " + updatedOwner.getName());
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for update: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for update: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${owner.queue.delete}")
    public void handleOwnerDeleteRequest(Message message) {
        String routingKey = RabbitMQConfig.OWNER_DELETE_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("OwnerMessageListener: Received request on delete queue. CorrelationId: " + correlationId);

        try {
            Map<String, Object> deleteData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing OWNER_DELETE_KEY. Received data: " + deleteData);

            Long deleteId = ((Number) deleteData.get("id")).longValue();
            Long currentUserIdDelete = deleteData.get("currentUserId") != null ? ((Number) deleteData.get("currentUserId")).longValue() : null;
            Set<String> currentUserRolesDelete = objectMapper.convertValue(deleteData.get("currentUserRoles"), new TypeReference<Set<String>>() {});

            ownerService.deleteOwnerById(deleteId, currentUserIdDelete, currentUserRolesDelete);
            responsePayload = "Owner deleted successfully.";
            System.out.println("  Owner with ID " + deleteId + " deleted.");
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for delete: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for delete: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    
    
    
}