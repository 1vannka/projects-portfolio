package com.project3.services;

import com.project3.models.Owners;
import com.project3.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service 
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public Owners saveOwner(Owners owner) {

        return ownerRepository.save(owner);
    }

    @Transactional(readOnly = true)
    public Owners getOwnerById(Long id) {
        return ownerRepository.findById(id).orElse(null); 
    }

    @Transactional
    public Owners updateOwner(Owners owner) {

        return ownerRepository.save(owner);
    }

    @Transactional
    public void deleteOwnerById(Long id) {

        ownerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Owners> getOwnersByNamePaginated(String name, Pageable pageable) {
        return ownerRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Owners> findAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable);
    }
}