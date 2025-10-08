package com.project5.repositories;

import com.project5.models.Color;
import com.project5.models.Cats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cats, Long> {
    Page<Cats> findByColor(Color color, Pageable pageable);
    Page<Cats> findByOwnerId(Long ownerId, Pageable pageable);
    Page<Cats> findByColorAndOwnerId(Color color, Long ownerId, Pageable pageable);

    List<Cats> findAllByOwnerId(Long ownerId); 
}