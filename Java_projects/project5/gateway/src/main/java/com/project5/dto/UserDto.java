package com.project5.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO для аутентификации пользователя (логина)")
public record UserDto(
        @Schema(description = "Имя пользователя (логин)", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя пользователя не может быть пустым")
        String username,

        @Schema(description = "Пароль пользователя", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {
}
