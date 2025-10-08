package com.project5.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "DTO для владельцев котов")
public class OwnerDto {

    @Schema(description = "Уникальный идентификатор владельца", example = "1")
    private Long id;

    @Schema(description = "Имя владельца", example = "Алексей", required = true)
    @NotBlank(message = "Имя владельца не может быть пустым")
    private String name;

    @Schema(description = "Дата рождения владельца", example = "1985-03-22")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Schema(description = "ID пользователя, связанного с этим владельцем", example = "101")
    private Long userId; 

    public OwnerDto() {
    }

    public OwnerDto(Long id, String name, LocalDate birthDate, Long userId) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}