package com.project5.listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project5.config.RabbitMQConfig;
import com.project5.dto.CatDto;
import com.project5.models.Color;
import com.project5.models.Cats;
import com.project5.services.CatService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CatMessageListener {

    private final CatService catService;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CatMessageListener(CatService catService, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.catService = catService;
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

    @RabbitListener(queues = "${cat.queue.create}")
    public void handleCatCreateRequest(Message message) {
        String routingKey = RabbitMQConfig.CAT_CREATE_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("CatMessageListener: Received request on create queue. CorrelationId: " + correlationId);

        try {

            Map<String, Object> createRequestData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing CAT_CREATE_KEY. Received data: " + createRequestData);

            Long currentUserId = ((Number) createRequestData.get("currentUserId")).longValue();
            Set<String> currentUserRoles = objectMapper.convertValue(createRequestData.get("currentUserRoles"), new TypeReference<Set<String>>() {});

            CatDto catToCreate = new CatDto();
            catToCreate.setName((String) createRequestData.get("name"));
            
            if (createRequestData.get("birthDate") instanceof String) {
                catToCreate.setBirthDate(LocalDate.parse((String) createRequestData.get("birthDate")));
            }
            if (createRequestData.get("breed") instanceof String) {
                catToCreate.setBreed((String) createRequestData.get("breed"));
            }
            if (createRequestData.get("color") instanceof String) {
                catToCreate.setColor(Color.valueOf((String) createRequestData.get("color")));
            }

            Cats newCat = catService.createCat(catToCreate, currentUserId, currentUserRoles); 
            responsePayload = catService.toCatDto(newCat);
            responseStatus = HttpStatus.CREATED;
            System.out.println("  Кот создан: " + newCat.getName() + " с ID: " + newCat.getId() + ", Owner ID: " + newCat.getOwnerId());

        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for cat create: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for cat create: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${cat.queue.byId}")
    public void handleCatGetByIdRequest(Message message) {
        String routingKey = RabbitMQConfig.CAT_GET_BY_ID_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("CatMessageListener: Received request on getById queue. CorrelationId: " + correlationId);

        try {
            
            Map<String, Object> getIdData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            Long catId = ((Number) getIdData.get("id")).longValue();
            
            Long currentUserId = ((Number) getIdData.get("currentUserId")).longValue();
            Set<String> currentUserRoles = objectMapper.convertValue(getIdData.get("currentUserRoles"), new TypeReference<Set<String>>() {});
            System.out.println("  Processing CAT_GET_BY_ID_KEY. ID: " + catId + ", User: " + currentUserId);

            Cats cat = catService.getCatById(catId); 

            if (cat != null && !catService.hasAccessToCat(catId, currentUserId, currentUserRoles)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. You are not authorized to view this cat.");
            }

            if (cat == null) {
                responseStatus = HttpStatus.NOT_FOUND;
                errorMessage = "Cat not found with id: " + catId;
                responsePayload = createErrorMap(responseStatus, errorMessage);
            } else {
                responsePayload = catService.toCatDto(cat);
                System.out.println("  Cat found: " + cat.getName());
            }
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for cat getById: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for cat getById: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${cat.queue.all}")
    public void handleCatGetAllRequest(Message message) {
        String routingKey = RabbitMQConfig.CAT_GET_ALL_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("CatMessageListener: Received request on getAll queue. CorrelationId: " + correlationId);

        try {
            Map<String, Object> getAllParams = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing CAT_GET_ALL_KEY. Params: " + getAllParams);

            int page = (Integer) getAllParams.getOrDefault("page", 0);
            int size = (Integer) getAllParams.getOrDefault("size", 10);
            String sortString = (String) getAllParams.getOrDefault("sort", "id:ASC");

            Long ownerIdFilter = null;
            if (getAllParams.containsKey("ownerId") && getAllParams.get("ownerId") != null) {
                ownerIdFilter = ((Number) getAllParams.get("ownerId")).longValue();
            }
            Long currentUserId = ((Number) getAllParams.get("currentUserId")).longValue();
            Set<String> currentUserRoles = objectMapper.convertValue(getAllParams.get("currentUserRoles"), new TypeReference<Set<String>>() {});
            String colorFilterStr = (String) getAllParams.get("color");

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

            Page<Cats> catsPage;
            Color colorFilter = null;
            if (colorFilterStr != null && !colorFilterStr.isEmpty()) {
                try {
                    colorFilter = Color.valueOf(colorFilterStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid color: " + colorFilterStr);
                }
            }

            if (!currentUserRoles.contains("ROLE_ADMIN") && ownerIdFilter != null && !ownerIdFilter.equals(currentUserId)) {
                
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. You can only filter by your own cats.");
            }

            if (ownerIdFilter != null && colorFilter != null) {
                catsPage = catService.getCatsByColorAndOwnerId(colorFilter, ownerIdFilter, pageable);
            } else if (ownerIdFilter != null) {
                catsPage = catService.getCatsByOwnerId(ownerIdFilter, pageable);
            } else if (colorFilter != null) {
                catsPage = catService.getCatsByColor(colorFilter, pageable);
            } else {
                catsPage = catService.findAllCats(pageable);
            }

            if (!currentUserRoles.contains("ROLE_ADMIN") && ownerIdFilter == null) {
                List<Cats> filteredCats = catsPage.getContent().stream()
                        .filter(cat -> {
                            try {
                                return catService.hasAccessToCat(cat.getId(), currentUserId, currentUserRoles);
                            } catch (ResponseStatusException e) {
                                System.err.println("Error checking access for cat " + cat.getId() + ": " + e.getMessage());
                                return false; 
                            }
                        })
                        .collect(Collectors.toList());

                
                catsPage = new PageImpl<>(filteredCats, pageable, filteredCats.size());
            }

            Map<String, Object> pageResponse = new HashMap<>();
            pageResponse.put("content", catsPage.getContent().stream().map(catService::toCatDto).collect(Collectors.toList()));
            pageResponse.put("number", catsPage.getNumber());
            pageResponse.put("size", catsPage.getSize());
            pageResponse.put("totalElements", catsPage.getTotalElements());
            pageResponse.put("totalPages", catsPage.getTotalPages());
            pageResponse.put("last", catsPage.isLast());
            pageResponse.put("first", catsPage.isFirst());
            pageResponse.put("empty", catsPage.isEmpty());

            responsePayload = pageResponse;
            System.out.println("  Returned " + catsPage.getTotalElements() + " cats.");
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for cat getAll: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for cat getAll: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${cat.queue.update}")
    public void handleCatUpdateRequest(Message message) {
        String routingKey = RabbitMQConfig.CAT_UPDATE_KEY;
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("CatMessageListener: Received request on update queue. CorrelationId: " + correlationId);

        try {
            Map<String, Object> updateData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing CAT_UPDATE_KEY. Received data: " + updateData);

            Long updateId = ((Number) updateData.get("id")).longValue();

            Map<String, Object> catDtoMap = (Map<String, Object>) updateData.get("catDto");
            CatDto catUpdateDto = new CatDto();
            if (catDtoMap.containsKey("name")) {
                catUpdateDto.setName((String) catDtoMap.get("name"));
            }
            if (catDtoMap.containsKey("birthDate") && catDtoMap.get("birthDate") instanceof String) {
                catUpdateDto.setBirthDate(LocalDate.parse((String) catDtoMap.get("birthDate")));
            }
            if (catDtoMap.containsKey("breed")) {
                catUpdateDto.setBreed((String) catDtoMap.get("breed"));
            }
            if (catDtoMap.containsKey("color") && catDtoMap.get("color") instanceof String) {
                catUpdateDto.setColor(Color.valueOf((String) catDtoMap.get("color")));
            }
            if (catDtoMap.containsKey("ownerId") && catDtoMap.get("ownerId") != null) {
                catUpdateDto.setOwnerId(((Number) catDtoMap.get("ownerId")).longValue());
            }

            Long currentUserId = updateData.get("currentUserId") != null ? ((Number) updateData.get("currentUserId")).longValue() : null;
            Set<String> currentUserRoles = objectMapper.convertValue(updateData.get("currentUserRoles"), new TypeReference<Set<String>>() {});

            Cats updatedCat = catService.updateCat(updateId, catUpdateDto, currentUserId, currentUserRoles);
            responsePayload = catService.toCatDto(updatedCat);
            System.out.println("  Cat updated: " + updatedCat.getName());
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for cat update: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for cat update: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${cat.queue.delete}")
    public void handleCatDeleteRequest(Message message) {
        String routingKey = RabbitMQConfig.CAT_DELETE_KEY;
        MessageProperties messageProperties = message.getMessageProperties();
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("CatMessageListener: Received request on delete queue. CorrelationId: " + correlationId);

        try {
            Map<String, Object> deleteData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing CAT_DELETE_KEY. Received data: " + deleteData);

            Long deleteId = ((Number) deleteData.get("id")).longValue();
            Long currentUserId = deleteData.get("currentUserId") != null ? ((Number) deleteData.get("currentUserId")).longValue() : null;
            Set<String> currentUserRoles = objectMapper.convertValue(deleteData.get("currentUserRoles"), new TypeReference<Set<String>>() {});

            catService.deleteCatById(deleteId, currentUserId, currentUserRoles);

            responsePayload = Map.of(
                    "status", "success",
                    "message", "Cat with ID " + deleteId + " deleted successfully."
            );


            System.out.println("  Cat with ID " + deleteId + " deleted. Preparing JSON response.");
            responseStatus = HttpStatus.NO_CONTENT; 
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage); 
            System.err.println("  ResponseStatusException caught for cat delete: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage); 
            System.err.println("  General Exception caught for cat delete: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${cat.queue.addFriend}") 
    public void handleCatAddFriendRequest(Message message) {
        String routingKey = RabbitMQConfig.CAT_ADD_FRIEND_KEY; 
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("CatMessageListener: Received request on addFriend queue. CorrelationId: " + correlationId);

        try {
            Map<String, Object> friendData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing CAT_ADD_FRIEND_KEY. Received data: " + friendData);

            Long catId = ((Number) friendData.get("catId")).longValue();
            Long friendId = ((Number) friendData.get("friendId")).longValue();
            Long currentUserId = ((Number) friendData.get("currentUserId")).longValue();
            Set<String> currentUserRoles = objectMapper.convertValue(friendData.get("currentUserRoles"), new TypeReference<Set<String>>() {});

            Cats updatedCat = catService.addFriend(catId, friendId, currentUserId, currentUserRoles);
            responsePayload = catService.toCatDto(updatedCat);
            System.out.println("  Cat " + catId + " added friend " + friendId);
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for addFriend: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for addFriend: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }

    @RabbitListener(queues = "${cat.queue.removeFriend}") 
    public void handleCatRemoveFriendRequest(Message message) {
        String routingKey = RabbitMQConfig.CAT_REMOVE_FRIEND_KEY; 
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        Object responsePayload = null;
        HttpStatus responseStatus = HttpStatus.OK;
        String errorMessage = null;

        System.out.println("CatMessageListener: Received request on removeFriend queue. CorrelationId: " + correlationId);

        try {
            Map<String, Object> friendData = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            System.out.println("  Processing CAT_REMOVE_FRIEND_KEY. Received data: " + friendData);

            Long catId = ((Number) friendData.get("catId")).longValue();
            Long friendId = ((Number) friendData.get("friendId")).longValue();
            Long currentUserId = ((Number) friendData.get("currentUserId")).longValue();
            Set<String> currentUserRoles = objectMapper.convertValue(friendData.get("currentUserRoles"), new TypeReference<Set<String>>() {});

            Cats updatedCat = catService.removeFriend(catId, friendId, currentUserId, currentUserRoles);
            responsePayload = catService.toCatDto(updatedCat);
            System.out.println("  Cat " + catId + " removed friend " + friendId);
        } catch (ResponseStatusException e) {
            responseStatus = (HttpStatus) e.getStatusCode();
            errorMessage = e.getReason();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  ResponseStatusException caught for removeFriend: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error: " + e.getMessage();
            responsePayload = createErrorMap(responseStatus, errorMessage);
            System.err.println("  General Exception caught for removeFriend: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendReply(replyTo, correlationId, responsePayload, responseStatus, routingKey);
        }
    }
}