package com.project5.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO для котов")
public record CatDto(

        @Schema(description = "Уникальный идентификатор кота", example = "1")
        Long id,

        @Schema(description = "Имя кота", example = "Дворняга", required = true)
        @NotNull
        String name,

        @Schema(description = "Дата рождения кота", example = "2019-05-10")
        LocalDate birthDate,

        @Schema(description = "Порода кота", example = "Сиамский")
        String breed,

        @Schema(description = "Цвет кота", example = "BLACK")
        Color color,

        @Schema(description = "ID владельца кота", example = "42")
        Long ownerId ,

        @Schema(description = "Список ID друзей кота", example = "[2, 3, 5]")
        List<Long> catFriendsId
) {

}
