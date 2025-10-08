package com.project5.services;

import com.project5.dto.OwnerDto;
import com.project5.models.Owners;
import com.project5.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Page<Owners> findAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable);
    }

    public Owners getOwnerById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Владелец не найден с ID: " + id));
    }

    public Page<Owners> getOwnersByNamePaginated(String name, Pageable pageable) {
        return ownerRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public Owners createOwner(OwnerDto ownerDto) {
        
        if (ownerDto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID владельца (User ID) должен быть предоставлен для создания.");
        }

        if (ownerRepository.existsById(ownerDto.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Владелец с ID (User ID) " + ownerDto.getId() + " уже существует.");
        }

        Owners owner = new Owners();

        owner.setId(ownerDto.getId());
        owner.setName(ownerDto.getName()); 

        owner.setBirthDate(ownerDto.getBirthDate() != null ? ownerDto.getBirthDate() : LocalDate.now());
        owner.setUserId(ownerDto.getId());

        return ownerRepository.save(owner);
    }

    @Transactional
    public Owners updateOwner(Long id, OwnerDto ownerDto, Long currentUserId, Set<String> currentUserRoles) {
        Owners existingOwner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Владелец не найден с ID: " + id));

        
        if (!hasAccessToOwner(id, currentUserId, currentUserRoles)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен. У вас нет прав для обновления информации об этом владельце.");
        }
        if (ownerDto.getName() != null) {
            existingOwner.setName(ownerDto.getName());
        }
        if (ownerDto.getBirthDate() != null) {
            existingOwner.setBirthDate(ownerDto.getBirthDate());
        }

        return ownerRepository.save(existingOwner);
    }

    @Transactional
    
    public void deleteOwnerById(Long id, Long currentUserId, Set<String> currentUserRoles) {
        Owners ownerToDelete = ownerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Владелец не найден с ID: " + id));

        
        if (!hasAccessToOwner(id, currentUserId, currentUserRoles)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен. У вас нет прав для удаления этого владельца.");
        }

        
        
        ownerRepository.delete(ownerToDelete);
    }

    public OwnerDto toOwnerDto(Owners owner) {
        if (owner == null) return null;
        
        
        return new OwnerDto(owner.getId(), owner.getName(), owner.getBirthDate(), null); 
    }

    public boolean hasAccessToOwner(Long ownerId, Long currentUserId, Set<String> currentUserRoles) {
        
        if (currentUserRoles.contains("ROLE_ADMIN")) {
            return true;
        }
        
        if (currentUserId == null) {
            return false;
        }
        return ownerId.equals(currentUserId);
    }
}