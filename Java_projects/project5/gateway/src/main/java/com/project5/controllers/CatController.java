package com.project5.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project5.config.RabbitMQConfig;
import com.project5.dto.CatDto;
import com.project5.dto.Color;
import com.project5.security.MyCustomUserDetails;
import com.project5.services.MessagingGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

import java.time.LocalDate; 
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cats")
@Tag(name = "Коты", description = "Методы для работы с котами")
@SecurityRequirement(name = "bearerAuth")
public class CatController {

    private final MessagingGateway messagingGateway;
    private final ObjectMapper objectMapper;

    @Autowired
    public CatController(MessagingGateway messagingGateway, ObjectMapper objectMapper) {
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

    @Operation(summary = "Получить всех котов")
    @GetMapping
    public ResponseEntity<Page<CatDto>> getAllCats(
            @Parameter(description = "Фильтр по цвету кота", schema = @Schema(implementation = Color.class))
            @RequestParam(required = false) Color color,
            @Parameter(description = "Фильтр по ID владельца кота")
            @RequestParam(required = false) Long ownerId, 
            @Parameter(description = "Параметры пагинации (page, size, sort)")
            Pageable pageable) throws ExecutionException, InterruptedException {

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("page", pageable.getPageNumber());
        requestParams.put("size", pageable.getPageSize());
        requestParams.put("sort", pageable.getSort().toString());
        if (color != null) requestParams.put("color", color.name());
        if (ownerId != null) requestParams.put("ownerId", ownerId); 

        requestParams.put("currentUserId", getCurrentUserId());
        requestParams.put("currentUserRoles", getCurrentUserRoles());

        Map<String, Object> pageData = messagingGateway.sendCatRequest(
                RabbitMQConfig.CAT_GET_ALL_KEY,
                requestParams,
                new TypeReference<Map<String, Object>>() {}
        ).get();

        List<CatDto> content = objectMapper.convertValue(pageData.get("content"), new TypeReference<List<CatDto>>() {});
        int page = (Integer) pageData.get("number");
        int size = (Integer) pageData.get("size");
        long totalElements = ((Number) pageData.get("totalElements")).longValue();

        Page<CatDto> catDtoPage = new PageImpl<>(content, pageable, totalElements);
        return ResponseEntity.ok(catDtoPage);
    }

    @Operation(summary = "Получить кота по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getCatById(@Parameter(description = "ID кота для поиска", required = true) @PathVariable Long id)
            throws ExecutionException, InterruptedException {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("id", id);
        requestParams.put("currentUserId", getCurrentUserId());
        requestParams.put("currentUserRoles", getCurrentUserRoles());

        CatDto catDto = messagingGateway.sendCatRequest(RabbitMQConfig.CAT_GET_BY_ID_KEY, requestParams, CatDto.class).get();

        if (catDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id: " + id);
        }

        return ResponseEntity.ok(catDto);
    }

    @Operation(summary = "Создать нового кота")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CatDto> createCat(@Parameter(description = "Объект кота для создания") @RequestBody CatDto catDto)
            throws ExecutionException, InterruptedException {

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Не удалось определить ID аутентифицированного пользователя.");
        }

        Map<String, Object> catData = new HashMap<>();
        catData.put("name", catDto.name());
        if (catDto.birthDate() != null) {
            catData.put("birthDate", catDto.birthDate().toString()); 
        } else {
            catData.put("birthDate", LocalDate.now().toString()); 
        }
        if (catDto.breed() != null) {
            catData.put("breed", catDto.breed());
        }
        if (catDto.color() != null) {
            catData.put("color", catDto.color().name()); 
        }

        catData.put("ownerId", currentUserId);
        catData.put("currentUserId", currentUserId); 
        catData.put("currentUserRoles", getCurrentUserRoles());

        try {
            CatDto savedCatDto = messagingGateway.sendCatRequest(RabbitMQConfig.CAT_CREATE_KEY, catData, CatDto.class).get();
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCatDto);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ResponseStatusException) {
                
                throw (ResponseStatusException) cause;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create cat: " + cause.getMessage(), cause);
        }
    }


    @Operation(summary = "Обновить информацию о коте")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CatDto> updateCat(@Parameter(description = "ID кота для обновления") @PathVariable Long id, @Parameter(description = "Обновлённые данные кота") @RequestBody CatDto catDto)
            throws ExecutionException, InterruptedException {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Не удалось определить ID аутентифицированного пользователя.");
        }
        Set<String> currentUserRoles = getCurrentUserRoles();

        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("id", id);
            requestData.put("catDto", objectMapper.convertValue(catDto, new TypeReference<Map<String, Object>>() {})); 
            requestData.put("currentUserId", currentUserId);
            requestData.put("currentUserRoles", currentUserRoles);

            CatDto updatedCatDto = messagingGateway.sendCatRequest(RabbitMQConfig.CAT_UPDATE_KEY, requestData, CatDto.class).get();

            if (updatedCatDto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id: " + id);
            }

            return ResponseEntity.ok(updatedCatDto);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ResponseStatusException) {
                throw (ResponseStatusException) cause;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update cat: " + cause.getMessage(), cause);
        }
    }

    @Operation(summary = "Удалить кота")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCat(@Parameter(description = "ID кота для удаления") @PathVariable Long id)
            throws ExecutionException, InterruptedException {
        System.out.println("DEBUG: CatController.deleteCat entered for ID: " + id + ", User: " +  getCurrentUserId());
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Не удалось определить ID аутентифицированного пользователя.");
        }
        Set<String> currentUserRoles = getCurrentUserRoles();

        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("id", id);
            requestData.put("currentUserId", currentUserId);
            requestData.put("currentUserRoles", currentUserRoles);

            messagingGateway.sendCatRequest(RabbitMQConfig.CAT_DELETE_KEY, requestData, new TypeReference<Map<String, String>>() {}).get();

            return ResponseEntity.noContent().build();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ResponseStatusException) {
                throw (ResponseStatusException) cause;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete cat: " + cause.getMessage(), cause);
        }
    }

    @PostMapping("/{catId}/friends/{friendId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> addFriend(@PathVariable Long catId, @PathVariable Long friendId)
            throws ExecutionException, InterruptedException {
        Long currentUserId = getCurrentUserId();
        Set<String> currentUserRoles = getCurrentUserRoles();

        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("catId", catId);
            requestData.put("friendId", friendId);
            requestData.put("currentUserId", currentUserId);
            requestData.put("currentUserRoles", currentUserRoles);

            messagingGateway.sendCatRequest(RabbitMQConfig.CAT_ADD_FRIEND_KEY, requestData, String.class).get();
            return ResponseEntity.ok().build();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ResponseStatusException) {
                throw (ResponseStatusException) cause;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add friend: " + cause.getMessage(), cause);
        }
    }

    @DeleteMapping("/{catId}/friends/{friendId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> removeFriend(@PathVariable Long catId, @PathVariable Long friendId)
            throws ExecutionException, InterruptedException {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Не удалось определить ID аутентифицированного пользователя.");
        }
        Set<String> currentUserRoles = getCurrentUserRoles();

        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("catId", catId);
            requestData.put("friendId", friendId);
            requestData.put("currentUserId", currentUserId);
            requestData.put("currentUserRoles", currentUserRoles);

            messagingGateway.sendCatRequest(RabbitMQConfig.CAT_REMOVE_FRIEND_KEY, requestData, String.class).get();
            return ResponseEntity.noContent().build();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ResponseStatusException) {
                throw (ResponseStatusException) cause;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to remove friend: " + cause.getMessage(), cause);
        }
    }
}