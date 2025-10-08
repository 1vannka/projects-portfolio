package com.project4.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name ="roles")
public class Role {
    @Id @GeneratedValue
    @Schema(description = "Уникальный идентификатор роли", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private long Id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Название роли (например, ROLE_USER, ROLE_ADMIN)", example = "ROLE_USER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}
}
