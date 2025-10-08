package com.project5.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project5.models.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects; 

@Schema(description = "DTO для котов")
public class CatDto {

    @Schema(description = "Уникальный идентификатор кота", example = "1")
    private Long id;

    @Schema(description = "Имя кота", example = "Дворняга", required = true)
    @NotBlank(message = "Имя кота не может быть пустым")
    private String name;

    @Schema(description = "Дата рождения кота", example = "2019-05-10")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Schema(description = "Порода кота", example = "Сиамский")
    private String breed;

    @Schema(description = "Цвет кота", example = "BLACK")
    private Color color;

    @Schema(description = "ID владельца кота", example = "42")
    private Long ownerId;

    @Schema(description = "Список ID друзей кота", example = "[2, 3, 5]")
    private List<Long> catFriendsId;

    public CatDto() {
    }

    public CatDto(Long id, String name, LocalDate birthDate, String breed, Color color, Long ownerId, List<Long> catFriendsId) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.breed = breed;
        this.color = color;
        this.ownerId = ownerId;
        this.catFriendsId = catFriendsId;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getCatFriendsId() {
        return catFriendsId;
    }

    public void setCatFriendsId(List<Long> catFriendsId) {
        this.catFriendsId = catFriendsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatDto catDto = (CatDto) o;
        return Objects.equals(id, catDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}