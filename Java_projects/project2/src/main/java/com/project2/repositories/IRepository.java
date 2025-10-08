package com.project2.repositories;

import java.util.List;

/**
 * A generic interface for a repository that provides basic CRUD operations for an entity.
 *
 * @param <T> The type of the entity managed by this repository.
 */
public interface IRepository<T> {
    /**
     * Saves a new entity to the data store.
     *
     * @param entity The entity to be saved.
     * @return The saved entity.
     */
    T save(T entity);

    /**
     * Deletes an entity from the data store based on its ID.
     *
     * @param id The ID of the entity to be deleted.
     */
    void deleteById(long id);

    /**
     * Deletes a given entity from the data store.
     *
     * @param entity The entity to be deleted.
     */
    void deleteByEntity(T entity);

    /**
     * Deletes all entities from the data store.
     */
    void deleteAll();

    /**
     * Updates an existing entity in the data store.
     *
     * @param entity The entity with updated information.
     * @return The updated entity.
     */
    T update(T entity);

    /**
     * Retrieves an entity from the data store based on its ID.
     *
     * @param id The ID of the entity to be retrieved.
     * @return The entity with the given ID, or null if not found.
     */
    T getById(long id);

    /**
     * Retrieves all entities from the data store.
     *
     * @return A list of all entities in the data store.
     */
    List<T> getAll();
}