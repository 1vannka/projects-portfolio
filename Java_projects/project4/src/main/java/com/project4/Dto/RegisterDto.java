package com.project4.Dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO для регистрации нового пользователя и связанного с ним владельца")
public record RegisterDto(
        @Schema(description = "Имя пользователя (логин)", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов")
        String username,

        @Schema(description = "Пароль пользователя", example = "Pa$$word", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, max = 100, message = "Пароль должен содержать от 6 до 100 символов")
        String password,

        @Schema(description = "Имя владельца (будет использоваться для создания сущности Owners)", example = "Иван Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя для профиля владельца не может быть пустым")
        String name
) {
}
