package com.project3.repositories;

import com.project3.models.Owners;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface OwnerRepository extends JpaRepository<Owners, Long> {
    Page<Owners> findByNameContainingIgnoreCase(String name, Pageable pageable);
}