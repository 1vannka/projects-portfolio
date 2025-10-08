package com.project3.controllers;

import com.project3.Dto.CatDto;
import com.project3.Dto.DtoMapper;
import com.project3.models.Cats;
import com.project3.models.Color;
import com.project3.services.CatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/cats")
@Tag(name = "Коты", description = "Методы для работы с котами")
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
            @RequestParam(required = false) Color color,
            @RequestParam(required = false) Long ownerId,
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
    public ResponseEntity<CatDto> getCatById(@PathVariable Long id) {
        Cats cat = catService.getCatById(id);

        if (cat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id: " + id);
        }

        CatDto catDto = dtoMapper.toCatDto(cat);
        return ResponseEntity.ok(catDto);
    }

    @Operation(summary = "Создать нового кота")
    @PostMapping
    public ResponseEntity<CatDto> createCat(@Parameter(description = "Объект кота для создания") @RequestBody CatDto catDto) {
        try {
            Cats savedCat = catService.createCatFromDto(catDto);
            CatDto savedCatDto = dtoMapper.toCatDto(savedCat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCatDto);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Обновить информацию о коте")
    @PutMapping("/{id}")
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
    public ResponseEntity<Void> deleteCat(@Parameter(description = "ID кота для удаления") @PathVariable Long id) {
        catService.deleteCatById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{catId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long catId, @PathVariable Long friendId) {
        catService.addFriend(catId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{catId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long catId, @PathVariable Long friendId) {
        catService.removeFriend(catId, friendId);
        return ResponseEntity.noContent().build();
    }
}