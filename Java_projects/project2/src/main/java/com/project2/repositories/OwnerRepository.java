package com.project2.repositories;

import com.project2.models.Owners;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implementation of the {@link IRepository} interface for the {@link Owners} entity.
 * Provides methods for performing CRUD operations on owners in the database.
 */
public class OwnerRepository implements IRepository<Owners> {
    private final EntityManager em;

    public OwnerRepository(EntityManager em) {
        this.em = em;
    }

    /**
     * Saves a new owner entity to the database.
     *
     * @param entity The {@link Owners} entity to be saved.
     * @return The saved {@link Owners} entity.
     */
    @Override
    public Owners save(Owners entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }

    /**
     * Deletes an owner entity from the database based on its ID.
     *
     * @param id The ID of the {@link Owners} entity to be deleted.
     */
    @Override
    public void deleteById(long id) {
        em.getTransaction().begin();
        Owners owner = em.find(Owners.class, id);
        if (owner != null) {
            em.remove(owner);
        }
        em.getTransaction().commit();
    }

    /**
     * Deletes a given owner entity from the database.
     *
     * @param entity The {@link Owners} entity to be deleted.
     */
    @Override
    public void deleteByEntity(Owners entity) {
        em.getTransaction().begin();
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        em.getTransaction().commit();
    }

    /**
     * Deletes all owner entities from the database.
     */
    @Override
    public void deleteAll() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Owners").executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * Updates an existing owner entity in the database.
     *
     * @param entity The {@link Owners} entity with updated information.
     * @return The updated {@link Owners} entity.
     */
    @Override
    public Owners update(Owners entity) {
        em.getTransaction().begin();
        Owners mergedEntity = em.merge(entity);
        em.getTransaction().commit();
        return mergedEntity;
    }

    /**
     * Retrieves an owner entity from the database based on its ID.
     *
     * @param id The ID of the {@link Owners} entity to be retrieved.
     * @return The {@link Owners} entity with the given ID, or null if not found.
     */
    @Override
    public Owners getById(long id) {
        return em.find(Owners.class, id);
    }
    /**
     * Retrieves all owner entities from the database.
     *
     * @return A list of all {@link Owners} entities in the database.
     */
    @Override
    public List<Owners> getAll() {
        TypedQuery<Owners> query = em.createQuery("SELECT o FROM Owners o", Owners.class);
        return query.getResultList();
    }
}