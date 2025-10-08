package com.project4.services;

import com.project4.Dto.CatDto;
import com.project4.Dto.DtoMapper;
import com.project4.models.Cats;
import com.project4.models.Color;
import com.project4.models.Owners;
import com.project4.models.Users;
import com.project4.repositories.CatRepository;
import com.project4.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service 
public class CatService {

    private final CatRepository catRepository;
    private final DtoMapper dtoMapper;
    private final UserRepository userRepository;

    @Autowired
    public CatService(CatRepository catRepository, DtoMapper dtoMapper, UserRepository userRepository) {
        this.catRepository = catRepository;
        this.dtoMapper = dtoMapper;
        this.userRepository = userRepository;
    }

    public Cats getCatById(Long id) throws ResponseStatusException {
        return catRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found with id " + id));
    }

    public void deleteCatById(Long id) {
        catRepository.deleteById(id);
    }

    public Page<Cats> getCatsByColorPaginated(Color color, Pageable pageable) {
        return catRepository.findByColor(color, pageable);
    }

    public Page<Cats> getCatsByOwnerIdPaginated(Long ownerId, Pageable pageable) {
        return catRepository.findByOwnerId(ownerId, pageable);
    }

    public Page<Cats> getAllCatsPaginated(Pageable pageable) {
        return catRepository.findAll(pageable);
    }

    public Page<Cats> getCatsByColorAndOwnerPaginated(Color color, Long ownerId, Pageable pageable) {
        return catRepository.findByColorAndOwnerId(color, ownerId, pageable);
    }

    public Cats createCatFromDto(CatDto catDto, Authentication authentication) {
        Cats cat = dtoMapper.toCatEntity(catDto);
        cat.setOwner(getOwner(authentication));
        return catRepository.save(cat);
    }

    
    @Transactional
    public Cats updateCatFromDto(Long id, CatDto catDto) {
        Cats existingCat = catRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cat not found with id: " + id));

        Cats updatedCat = dtoMapper.updateCatEntity(existingCat, catDto);

        return catRepository.save(updatedCat);
    }

    @Transactional
    public void addFriend(Long catId, Long friendId) {
        Cats cat = catRepository.findById(catId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Cats friend = catRepository.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        cat.getFriends().add(friend);
        friend.getFriends().add(cat);
        catRepository.save(cat);
        catRepository.save(friend);
    }

    @Transactional
    public void removeFriend(Long catId, Long friendId) {
        Cats cats = catRepository.findById(catId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Cats friend = catRepository.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        cats.getFriends().remove(friend);
        friend.getFriends().remove(cats);
        catRepository.save(cats);
        catRepository.save(friend);
    }

    public boolean IsOwner(Long catId, Authentication authentication) {
        Cats cat = getCatById(catId);
        Owners owner = cat.getOwner();
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

    public Owners getOwner(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<Users> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get().getOwner();
        }
        return null;
    }
}