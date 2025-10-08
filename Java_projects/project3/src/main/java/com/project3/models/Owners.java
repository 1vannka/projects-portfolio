package com.project3.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "owners")
public class Owners {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        private String name;

    @Column(name = "birthdate")
        private LocalDate birthDate;

        @OneToMany(mappedBy = "owner")
    private List<Cats> cats;

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

        public List<Cats> getCats() {
        return cats;
    }

        public void setCats(List<Cats> cats) {
            this.cats = cats;
    }
}