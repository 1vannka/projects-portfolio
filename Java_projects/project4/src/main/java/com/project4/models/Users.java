package com.project4.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Objects; 
import java.util.Set;

@Entity
@Table(name = "users") 
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Имя пользователя (логин)", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username; 

    @Column(nullable = false)
    @Schema(description = "Хэшированный пароль пользователя. Не возвращается в ответах API.", accessMode = Schema.AccessMode.WRITE_ONLY, requiredMode = Schema.RequiredMode.REQUIRED)
    private String password; 

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "Набор ролей, назначенных пользователю. Определяет уровень доступа пользователя.")
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) 
    @Schema(description = "Связанный профиль владельца, если пользователь является владельцем. Загружается лениво.")
    private Owners owner; 

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Owners getOwner() { return owner; }
    public void setOwner(Owners owner) { this.owner = owner; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }


}