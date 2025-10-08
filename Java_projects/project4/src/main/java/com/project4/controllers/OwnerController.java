package com.project4.controllers;

import com.project4.Dto.DtoMapper;
import com.project4.Dto.OwnerDto;
import com.project4.models.Owners;
import com.project4.services.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/v1/owners")
@Tag(name = "Владельцы", description = "Операции для управления владельцами котов")
@SecurityRequirement(name = "cookieAuth")
public class OwnerController {

    private final OwnerService ownerService;
    private final DtoMapper dtoMapper; 

    @Autowired
    public OwnerController(OwnerService ownerService, DtoMapper dtoMapper) {
        this.ownerService = ownerService;
        this.dtoMapper = dtoMapper;
    }

    @Operation(summary = "Получить список всех владельцев")
    @GetMapping
    public ResponseEntity<Page<OwnerDto>> getAllOwnersDto(Pageable pageable, String name) {
        Page<Owners> ownerPage;

        if (name != null && !name.trim().isEmpty()) {
            ownerPage = ownerService.getOwnersByNamePaginated(name, pageable);
        } else {
            ownerPage = ownerService.findAllOwners(pageable);
        }
            Page<OwnerDto> ownerDtoPage = ownerPage.map(dtoMapper::toOwnerDto);
            return ResponseEntity.ok(ownerDtoPage);
    }

    @Operation(summary = "Получить владельца по ID")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(@Parameter(description = "ID владельца") @PathVariable Long id) {
        Owners owner = ownerService.getOwnerById(id);

        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found with id: " + id);
            
        }

        OwnerDto ownerDto = dtoMapper.toOwnerDto(owner);
        return ResponseEntity.ok(ownerDto);
    }

    @Operation(summary = "Создать нового владельца")
    @PostMapping
    public ResponseEntity<OwnerDto> createOwner(@Parameter(description = "Объект владельца для создания") @RequestBody OwnerDto ownerDto) {
        
        Owners ownerToSave = dtoMapper.toOwnerEntity(ownerDto);
        Owners savedOwner = ownerService.saveOwner(ownerToSave); 
        OwnerDto savedOwnerDto = dtoMapper.toOwnerDto(savedOwner);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOwnerDto);
    }

    @Operation(summary = "Обновить информацию о владельце")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @ownerService.IsOwner(#id, authentication)")
    public ResponseEntity<OwnerDto> updateOwner(@Parameter(description = "ID владельца для обновления") @PathVariable Long id,@Parameter(description = "Обновлённые данные владельца") @RequestBody OwnerDto ownerDto) {
        
        Owners existingOwner = ownerService.getOwnerById(id);
        if (existingOwner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found with id: " + id);
        }

        Owners updatedOwner = dtoMapper.updateOwnerEntity(existingOwner, ownerDto);
        Owners savedUpdatedOwner = ownerService.updateOwner(updatedOwner);

        OwnerDto updatedOwnerDto = dtoMapper.toOwnerDto(savedUpdatedOwner);
        return ResponseEntity.ok(updatedOwnerDto); 
    }

    @Operation(summary = "Удалить владельца")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @ownerService.IsOwner(#id, authentication)")
    public ResponseEntity<Void> deleteOwner(@Parameter(description = "ID владельца для удаления") @PathVariable Long id) {
        ownerService.deleteOwnerById(id);
        return ResponseEntity.noContent().build(); 
    }
}