package com.project5.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project5.config.RabbitMQConfig;
import com.project5.dto.OwnerDto;
import com.project5.security.MyCustomUserDetails; 
import com.project5.services.MessagingGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set; 
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/owners")
@Tag(name = "Владельцы", description = "Операции для управления владельцами котов")
@SecurityRequirement(name = "bearerAuth")
public class OwnerController {

    private final MessagingGateway messagingGateway;
    private final ObjectMapper objectMapper;

    @Autowired
    public OwnerController(MessagingGateway messagingGateway, ObjectMapper objectMapper) {
        this.messagingGateway = messagingGateway;
        this.objectMapper = objectMapper;
    }
        private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof MyCustomUserDetails) {
            return ((MyCustomUserDetails) principal).getId();
        } else {
            
            System.err.println("Warning: Principal is not MyCustomUserDetails. Cannot directly extract userId. Principal type: " + principal.getClass().getName());
            return null;
        }
    }
        private Set<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptySet();
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    @Operation(summary = "Получить список всех владельцев")
    @GetMapping
    public ResponseEntity<Page<OwnerDto>> getAllOwnersDto(Pageable pageable, @RequestParam(required = false) String name)
            throws ExecutionException, InterruptedException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("page", pageable.getPageNumber());
        requestParams.put("size", pageable.getPageSize());
        requestParams.put("sort", pageable.getSort().toString());
        if (name != null && !name.trim().isEmpty()) {
            requestParams.put("name", name);
        }

        requestParams.put("currentUserId", getCurrentUserId());
        requestParams.put("currentUserRoles", getCurrentUserRoles());

        Map<String, Object> pageData = messagingGateway.sendOwnerRequest(
                RabbitMQConfig.OWNER_GET_ALL_KEY,
                requestParams,
                new TypeReference<Map<String, Object>>() {}
        ).get();

        List<OwnerDto> content = objectMapper.convertValue(pageData.get("content"), new TypeReference<List<OwnerDto>>() {});
        int page = (Integer) pageData.get("number");
        int size = (Integer) pageData.get("size");
        long totalElements = (Long) pageData.get("totalElements");

        Page<OwnerDto> ownerDtoPage = new PageImpl<>(content, pageable, totalElements);
        return ResponseEntity.ok(ownerDtoPage);
    }

    @Operation(summary = "Получить владельца по ID")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(@Parameter(description = "ID владельца") @PathVariable Long id)
            throws ExecutionException, InterruptedException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("id", id);

        
        requestParams.put("currentUserId", getCurrentUserId());
        requestParams.put("currentUserRoles", getCurrentUserRoles());

        OwnerDto ownerDto = messagingGateway.sendOwnerRequest(RabbitMQConfig.OWNER_GET_BY_ID_KEY, requestParams, OwnerDto.class).get();

        if (ownerDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found with id: " + id);
        }

        return ResponseEntity.ok(ownerDto);
    }

    @Operation(summary = "Создать нового владельца")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OwnerDto> createOwner(@Parameter(description = "Объект владельца для создания") @RequestBody OwnerDto ownerDto)
            throws ExecutionException, InterruptedException {
        try {
            Map<String, Object> ownerData = objectMapper.convertValue(ownerDto, new TypeReference<Map<String, Object>>() {});
            
            ownerData.put("currentUserId", getCurrentUserId());
            ownerData.put("currentUserRoles", getCurrentUserRoles());

            OwnerDto savedOwnerDto = messagingGateway.sendOwnerRequest(RabbitMQConfig.OWNER_CREATE_KEY, ownerData, OwnerDto.class).get();

            return ResponseEntity.status(HttpStatus.CREATED).body(savedOwnerDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Обновить информацию о владельце")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OwnerDto> updateOwner(@Parameter(description = "ID владельца для обновления") @PathVariable Long id, @Parameter(description = "Обновлённые данные владельца") @RequestBody OwnerDto ownerDto)
            throws ExecutionException, InterruptedException {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("id", id);
            requestData.put("ownerDto", ownerDto);
            
            requestData.put("currentUserId", getCurrentUserId());
            requestData.put("currentUserRoles", getCurrentUserRoles());

            OwnerDto updatedOwnerDto = messagingGateway.sendOwnerRequest(RabbitMQConfig.OWNER_UPDATE_KEY, requestData, OwnerDto.class).get();

            if (updatedOwnerDto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found with id: " + id);
            }

            return ResponseEntity.ok(updatedOwnerDto);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ResponseStatusException) {
                throw (ResponseStatusException) cause;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update owner: " + cause.getMessage());
        }
    }

    @Operation(summary = "Удалить владельца")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteOwner(@Parameter(description = "ID владельца для удаления") @PathVariable Long id)
            throws ExecutionException, InterruptedException {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("id", id);
            
            requestData.put("currentUserId", getCurrentUserId());
            requestData.put("currentUserRoles", getCurrentUserRoles());

            String responseMessage = messagingGateway.sendOwnerRequest(RabbitMQConfig.OWNER_DELETE_KEY, requestData, String.class).get();

            if (responseMessage != null && responseMessage.contains("error")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for owner deletion.");
            }

            return ResponseEntity.noContent().build();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ResponseStatusException) {
                throw (ResponseStatusException) cause;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete owner: " + cause.getMessage());
        }
    }
}