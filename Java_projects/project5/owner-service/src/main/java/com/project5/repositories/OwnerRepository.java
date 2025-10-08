package com.project5.repositories;

import com.project5.models.Owners;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owners, Long> {
    Page<Owners> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Owners> findByUserId(Long userId);
}