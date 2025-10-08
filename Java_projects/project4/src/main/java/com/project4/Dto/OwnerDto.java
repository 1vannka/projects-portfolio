package com.project4.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO для владельцев котов")
public record OwnerDto(

        @Schema(description = "Уникальный идентификатор владельца", example = "1")
        Long id,

        @Schema(description = "Имя владельца", example = "Алексей")
        String name,

        @Schema(description = "Дата рождения владельца", example = "1985-03-22")
        LocalDate birthDate,

        @Schema(description = "Список котов, принадлежащих владельцу")
        List<CatDto> cats
) {
}
