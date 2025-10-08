package com.project2.services;

import com.project2.models.Cats;
import com.project2.repositories.CatRepository;

import java.util.List;

/**
 * Service class for managing {@link Cats} entities.
 * Provides methods to interact with the {@link CatRepository}.
 */
public class CatService {
    private final CatRepository catRepository;

    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }
    /**
     * Saves a new cat to the database.
     *
     * @param cat The {@link Cats} entity to be saved.
     * @return The saved {@link Cats} entity.
     */
    public Cats saveCat(Cats cat) {
        return catRepository.save(cat);
    }

    /**
     * Retrieves a cat from the database by its ID.
     *
     * @param id The ID of the cat to retrieve.
     * @return The {@link Cats} entity with the given ID, or null if not found.
     */
    public Cats getCatById(Long id) {
        return catRepository.getById(id);
    }

    /**
     * Retrieves all cats from the database.
     *
     * @return A list of all {@link Cats} entities in the database.
     */
    public List<Cats> getAllCats() {
        return catRepository.getAll();
    }

    /**
     * Updates an existing cat in the database.
     *
     * @param cat The {@link Cats} entity with updated information.
     * @return The updated {@link Cats} entity.
     */
    public Cats updateCat(Cats cat) {
        return catRepository.update(cat);
    }

    /**
     * Deletes a cat from the database.
     *
     * @param cat The {@link Cats} entity to be deleted.
     */
    public void deleteCat(Cats cat) {
        catRepository.deleteByEntity(cat);
    }

    /**
     * Deletes a cat from the database by its ID.
     *
     * @param id The ID of the cat to be deleted.
     */
    public void deleteCatById(Long id) {
        catRepository.deleteById(id);
    }
}