package com.project4.repositories;

import com.project4.models.Cats;
import com.project4.models.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatRepository extends JpaRepository<Cats, Long> {

    Page<Cats> findByColor(Color color, Pageable pageable);

    Page<Cats> findByOwnerId(Long ownerId, Pageable pageable);

    Page<Cats> findByColorAndOwnerId(Color color, Long ownerId, Pageable pageable);
}