package com.project2.repositories;

import com.project2.models.Cats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implementation of the {@link IRepository} interface for the {@link Cats} entity.
 * Provides methods for performing CRUD operations on cats in the database.
 */
public class CatRepository implements IRepository<Cats> {
    private final EntityManager em;

    public CatRepository(EntityManager em) {
        this.em = em;
    }

    /**
     * Saves a new cat entity to the database.
     *
     * @param entity The {@link Cats} entity to be saved.
     * @return The saved {@link Cats} entity.
     */
    @Override
    public Cats save(Cats entity) {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Ошибка при сохранении кота: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a cat entity from the database based on its ID.
     *
     * @param id The ID of the {@link Cats} entity to be deleted.
     */
    @Override
    public void deleteById(long id) {
        em.getTransaction().begin();
        Cats cat = em.find(Cats.class, id);
        if (cat != null) {
            em.remove(cat);
        }
        em.getTransaction().commit();
    }

    /**
     * Deletes a given cat entity from the database.
     *
     * @param entity The {@link Cats} entity to be deleted.
     */
    @Override
    public void deleteByEntity(Cats entity) {
        em.getTransaction().begin();
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        em.getTransaction().commit();
    }

    /**
     * Deletes all cat entities from the database.
     */
    @Override
    public void deleteAll() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Cats").executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * Updates an existing cat entity in the database.
     *
     * @param entity The {@link Cats} entity with updated information.
     * @return The updated {@link Cats} entity.
     */
    @Override
    public Cats update(Cats entity) {
        em.getTransaction().begin();
        Cats mergedEntity = em.merge(entity);
        em.getTransaction().commit();
        return mergedEntity;
    }

    /**
     * Retrieves a cat entity from the database based on its ID.
     *
     * @param id The ID of the {@link Cats} entity to be retrieved.
     * @return The {@link Cats} entity with the given ID, or null if not found.
     */
    @Override
    public Cats getById(long id) {
        return em.find(Cats.class, id);
    }

    /**
     * Retrieves all cat entities from the database.
     *
     * @return A list of all {@link Cats} entities in the database.
     */
    @Override
    public List<Cats> getAll() {
        TypedQuery<Cats> query = em.createQuery("SELECT c FROM Cats c", Cats.class);
        return query.getResultList();
    }
}