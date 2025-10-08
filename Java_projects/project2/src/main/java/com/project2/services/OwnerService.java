package com.project2.services;

import com.project2.models.Owners;
import com.project2.repositories.OwnerRepository;

import java.util.List;

/**
 * Service class for managing {@link Owners} entities.
 * Provides methods to interact with the {@link OwnerRepository}.
 */
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * Saves a new owner to the database.
     *
     * @param owner The {@link Owners} entity to be saved.
     * @return The saved {@link Owners} entity.
     */
    public Owners saveOwner(Owners owner) {
        return ownerRepository.save(owner);
    }

    /**
     * Retrieves an owner from the database by their ID.
     *
     * @param id The ID of the owner to retrieve.
     * @return The {@link Owners} entity with the given ID, or null if not found.
     */
    public Owners getOwnerById(Long id) {
        return ownerRepository.getById(id);
    }

    /**
     * Retrieves all owners from the database.
     *
     * @return A list of all {@link Owners} entities in the database.
     */
    public List<Owners> getAllOwners() {
        return ownerRepository.getAll();
    }

    /**
     * Updates an existing owner in the database.
     *
     * @param owner The {@link Owners} entity with updated information.
     * @return The updated {@link Owners} entity.
     */
    public Owners updateOwner(Owners owner) {
        return ownerRepository.update(owner);
    }

    /**
     * Deletes an owner from the database.
     *
     * @param owner The {@link Owners} entity to be deleted.
     */
    public void deleteOwner(Owners owner) {
        ownerRepository.deleteByEntity(owner);
    }

    /**
     * Deletes an owner from the database by their ID.
     *
     * @param id The ID of the owner to be deleted.
     */
    public void deleteOwnerById(Long id) {
        ownerRepository.deleteById(id);
    }
}