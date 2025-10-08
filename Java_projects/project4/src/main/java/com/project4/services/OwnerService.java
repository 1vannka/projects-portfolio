package com.project4.services;

import com.project4.models.Owners;
import com.project4.models.Users;
import com.project4.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service 
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }


    public Owners saveOwner(Owners owner) {
        return ownerRepository.save(owner);
    }


    public Owners getOwnerById(Long id) throws ResponseStatusException {
        return ownerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found with id " + id));
    }

    public Owners updateOwner(Owners owner) {

        return ownerRepository.save(owner);
    }

    public void deleteOwnerById(Long id) {

        ownerRepository.deleteById(id);
    }

    public Page<Owners> getOwnersByNamePaginated(String name, Pageable pageable) {
        return ownerRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Owners> findAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable);
    }

    public boolean IsOwner(Long id, Authentication authentication) {
        Owners owner = getOwnerById(id);
        Users user = owner.getUser();
        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            if (Objects.equals(username, user.getUsername())) {
                return true;
            }
        }
        return false;
    }
}