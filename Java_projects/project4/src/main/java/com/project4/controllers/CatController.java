package com.project4.controllers;

import com.project4.Dto.CatDto;
import com.project4.Dto.DtoMapper;
import com.project4.models.Cats;
import com.project4.models.Color;
import com.project4.services.CatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/cats")
@Tag(name = "Коты", description = "Методы для работы с котами")
@SecurityRequirement(name = "cookieAuth")
public class CatController {

    private final CatService catService;
    private final DtoMapper dtoMapper;

    @Autowired
    public CatController(CatService catService, DtoMapper dtoMapper) {
        this.catService = catService;
        this.dtoMapper = dtoMapper;
    }

    @Operation(summary = "Получить всех котов")
    @GetMapping
    public ResponseEntity<Page<CatDto>> getAllCats(
            @Parameter(description = "Фильтр по цвету кота", schema = @Schema(implementation = Color.class))
            @RequestParam(required = false) Color color,
            @Parameter(description = "Фильтр по ID владельца кота")
            @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Параметры пагинации (page, size, sort)")
            Pageable pageable)
    {
        Page<Cats> catsPage;

        if (color != null && ownerId != null) {
            catsPage = catService.getCatsByColorAndOwnerPaginated(color, ownerId, pageable);
        } else if (color != null) {
            catsPage = catService.getCatsByColorPaginated(color, pageable);
        } else if (ownerId != null) {
            catsPage = catService.getCatsByOwnerIdPaginated(ownerId, pageable);
        }
        else {
            catsPage = catService.getAllCatsPaginated(pageable);
        }

        Page<CatDto> catDtoPage = catsPage.map(dtoMapper::toCatDto);
        return ResponseEntity.ok(catDtoPage);
    }

    @Operation(summary = "Получить кота по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getCatById(@Parameter(description = "ID кота для поиска", required = true) @PathVariable Long id) {
        Cats cat = catService.getCatById(id);

        if (cat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id: " + id);
        }

        CatDto catDto = dtoMapper.toCatDto(cat);
        return ResponseEntity.ok(catDto);
    }

    @Operation(summary = "Создать нового кота")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CatDto> createCat(@Parameter(description = "Объект кота для создания") @RequestBody CatDto catDto, Authentication authentication) {
        try {
            Cats savedCat = catService.createCatFromDto(catDto, authentication);
            CatDto savedCatDto = dtoMapper.toCatDto(savedCat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCatDto);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Обновить информацию о коте")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @catService.IsOwner(#id, authentication)")
    public ResponseEntity<CatDto> updateCat(@Parameter(description = "ID кота для обновления") @PathVariable Long id, @Parameter(description = "Обновлённые данные кота") @RequestBody CatDto catDto) {
        try {
            Cats updatedCat = catService.updateCatFromDto(id, catDto);
            CatDto updatedCatDto = dtoMapper.toCatDto(updatedCat);
            return ResponseEntity.ok(updatedCatDto);
        } catch (ResponseStatusException e) {
            throw e;
        }
        catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Удалить кота")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @catService.IsOwner(#id, authentication)")
    public ResponseEntity<Void> deleteCat(@Parameter(description = "ID кота для удаления") @PathVariable Long id) {
        catService.deleteCatById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{catId}/friends/{friendId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @catService.IsOwner(#catId, authentication)")
    public ResponseEntity<Void> addFriend(@PathVariable Long catId, @PathVariable Long friendId) {
        catService.addFriend(catId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{catId}/friends/{friendId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @catService.IsOwner(#catId, authentication)")
    public ResponseEntity<Void> removeFriend(@PathVariable Long catId, @PathVariable Long friendId) {
        catService.removeFriend(catId, friendId);
        return ResponseEntity.noContent().build();
    }
}