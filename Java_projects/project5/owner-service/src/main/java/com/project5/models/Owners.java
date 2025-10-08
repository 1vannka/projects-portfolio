package com.project5.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "owners")
public class Owners {

    @Id
    private Long id;

    private String name;

    @Column(name = "birthdate")
    private LocalDate birthDate;

    @Column(name = "user_id", unique = true)
    private Long userId;

    
    public Owners() {
    }

    public Owners(Long id, String name, LocalDate birthDate, Long userId) {
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