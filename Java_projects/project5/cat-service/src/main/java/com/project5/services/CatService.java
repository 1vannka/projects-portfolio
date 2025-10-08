package com.project5.services;

import com.project5.dto.CatDto;
import com.project5.models.Color;
import com.project5.dto.OwnerDto;
import com.project5.models.Cats;
import com.project5.repositories.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CatService {

    private final CatRepository catRepository;
    private final OwnerServiceProxy ownerServiceProxy;

    @Autowired
    public CatService(CatRepository catRepository, OwnerServiceProxy ownerServiceProxy) {
        this.catRepository = catRepository;
        this.ownerServiceProxy = ownerServiceProxy;
    }

    public Page<Cats> findAllCats(Pageable pageable) {
        return catRepository.findAll(pageable);
    }

    public Cats getCatById(Long id) {
        return catRepository.findById(id).orElse(null);
    }

    public Page<Cats> getCatsByColor(Color color, Pageable pageable) {
        return catRepository.findByColor(color, pageable);
    }

    public Page<Cats> getCatsByOwnerId(Long ownerId, Pageable pageable) {
        return catRepository.findByOwnerId(ownerId, pageable);
    }

    public Page<Cats> getCatsByColorAndOwnerId(Color color, Long ownerId, Pageable pageable) {
        return catRepository.findByColorAndOwnerId(color, ownerId, pageable);
    }

    public List<Cats> getAllCatsByOwnerId(Long ownerId) {
        return catRepository.findAllByOwnerId(ownerId);
    }

    @Transactional
    public Cats createCat(CatDto catDto, Long currentUserId, Set<String> currentUserRoles) {
        
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ID пользователя не может быть null при создании кота.");
        }

        
        try {
            OwnerDto ownerDto = ownerServiceProxy.getOwnerById(currentUserId).get();
            ownerDto.setUserId(ownerDto.getId());
            
            if (ownerDto == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Владелец с ID: " + currentUserId + " не найден. Невозможно создать кота.");
            }
            
            if (ownerDto.getUserId() == null || !ownerDto.getUserId().equals(currentUserId)) {

                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Несоответствие ID владельца при создании кота.");
            }

            Cats cat = new Cats();
            cat.setName(catDto.getName());
            cat.setBirthDate(catDto.getBirthDate() != null ? catDto.getBirthDate() : LocalDate.now());
            cat.setBreed(catDto.getBreed());
            cat.setColor(catDto.getColor());
            cat.setOwnerId(currentUserId); 

            return catRepository.save(cat);
        } catch (ExecutionException | InterruptedException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ResponseStatusException) {
                throw (ResponseStatusException) cause;
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при связи с Owner Service: " + cause.getMessage(), cause);
        }
    }

    @Transactional
    public Cats updateCat(Long id, CatDto catDto, Long currentUserId, Set<String> currentUserRoles) {
        Cats existingCat = catRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id: " + id));

        if (!hasAccessToCat(id, currentUserId, currentUserRoles)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. You are not authorized to update this cat.");
        }

        if (catDto.getName() != null) {
            existingCat.setName(catDto.getName());
        }
        if (catDto.getBirthDate() != null) {
            existingCat.setBirthDate(catDto.getBirthDate());
        }
        if (catDto.getBreed() != null) {
            existingCat.setBreed(catDto.getBreed());
        }
        if (catDto.getColor() != null) {
            existingCat.setColor(catDto.getColor());
        }
        
        if (catDto.getOwnerId() != null && !catDto.getOwnerId().equals(existingCat.getOwnerId())) {
            
            try {
                OwnerDto newOwnerDto = ownerServiceProxy.getOwnerById(catDto.getOwnerId()).get();
                if (newOwnerDto == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New owner with id: " + catDto.getOwnerId() + " not found.");
                }

                if (!currentUserRoles.contains("ROLE_ADMIN")) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Only admin can change cat's owner.");
                }
                existingCat.setOwnerId(catDto.getOwnerId());
            } catch (ExecutionException | InterruptedException e) {
                Throwable cause = e.getCause();
                if (cause instanceof ResponseStatusException) {
                    throw (ResponseStatusException) cause;
                }
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to communicate with Owner Service for owner change: " + cause.getMessage(), cause);
            }
        }
        Cats savedCat = catRepository.save(existingCat);

        if (savedCat.getFriends() != null) {
            savedCat.getFriends().size();

        }

        return savedCat;
    }

    @Transactional
    public void deleteCatById(Long id, Long currentUserId, Set<String> currentUserRoles) {
        Cats cat = catRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id: " + id));

        
        if (!hasAccessToCat(id, currentUserId, currentUserRoles) && !currentUserRoles.contains("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. You are not authorized to delete this cat.");
        }

        
        for (Cats friend : cat.getFriends()) {
            friend.getFriends().remove(cat);
            catRepository.save(friend); 
        }
        
        cat.getFriends().clear();
        catRepository.save(cat); 
        System.out.println("DEBUG: CatService.deleteCatById - Before deletion for ID: " + id);
        catRepository.delete(cat);
        System.out.println("DEBUG: CatService.deleteCatById - After deletion for ID: " + id);
    }

    @Transactional
    public Cats addFriend(Long catId, Long friendId, Long currentUserId, Set<String> currentUserRoles) {
        Cats cat = catRepository.findById(catId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id: " + catId));
        Cats friend = catRepository.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend cat not found with id: " + friendId));

        if (catId.equals(friendId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A cat cannot be friends with itself.");
        }

        
        if (!hasAccessToCat(catId, currentUserId, currentUserRoles)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. You are not authorized to add friends for this cat.");
        }

        cat.getFriends().add(friend);
        friend.getFriends().add(cat); 
        catRepository.save(friend); 
        return catRepository.save(cat);
    }

    @Transactional
    public Cats removeFriend(Long catId, Long friendId, Long currentUserId, Set<String> currentUserRoles) {
        Cats cat = catRepository.findById(catId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id: " + catId));
        Cats friend = catRepository.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend cat not found with id: " + friendId));

        if (!hasAccessToCat(catId, currentUserId, currentUserRoles)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. You are not authorized to remove friends for this cat.");
        }

        if (!cat.getFriends().contains(friend)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cats are not friends.");
        }

        cat.getFriends().remove(friend);
        friend.getFriends().remove(cat); 
        catRepository.save(friend); 
        return catRepository.save(cat);
    }

    @Transactional(readOnly = true)
    public CatDto toCatDto(Cats cat) {
        if (cat == null) return null;
        List<Long> friendIds = cat.getFriends().stream()
                .map(Cats::getId)
                .collect(Collectors.toList());
        return new CatDto(cat.getId(), cat.getName(), cat.getBirthDate(), cat.getBreed(), cat.getColor(), cat.getOwnerId(), friendIds);
    }

    
    public boolean hasAccessToCat(Long catId, Long currentUserId, Set<String> currentUserRoles) {
        if (currentUserRoles.contains("ROLE_ADMIN")) {
            return true;
        }
        if (currentUserId == null) {
            return false;
        }
        Optional<Cats> catOptional = catRepository.findById(catId);
        if (catOptional.isEmpty()) {
            return false; 
        }
        Cats cat = catOptional.get();

        try {
            return cat.getOwnerId().equals(currentUserId);

        } catch (Exception e) {
            System.err.println("Error verifying owner access for cat " + catId + ": " + e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to verify owner access due to internal error: " + e.getMessage());
        }
    }
}