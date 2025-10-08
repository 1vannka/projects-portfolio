package com.project3.services;

import com.project3.Dto.DtoMapper;
import com.project3.Dto.CatDto;
import com.project3.exceptions.CatNotFoundException;
import com.project3.models.Cats;
import com.project3.models.Color;
import com.project3.repositories.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service 
public class CatService {

    private final CatRepository catRepository;
    private final DtoMapper dtoMapper;

    @Autowired
    public CatService(CatRepository catRepository, DtoMapper dtoMapper) {
        this.catRepository = catRepository;
        this.dtoMapper = dtoMapper;
    }

    @Transactional
    public Cats getCatById(Long id) {
        return catRepository.findById(id)
                .orElseThrow(() -> new CatNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public void deleteCatById(Long id) {

        catRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Cats> getCatsByColorPaginated(Color color, Pageable pageable) {
        return catRepository.findByColor(color, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Cats> getCatsByOwnerIdPaginated(Long ownerId, Pageable pageable) {
        return catRepository.findByOwnerId(ownerId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Cats> getAllCatsPaginated(Pageable pageable) {
        return catRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Cats> getCatsByColorAndOwnerPaginated(Color color, Long ownerId, Pageable pageable) {
        return catRepository.findByColorAndOwnerId(color, ownerId, pageable);
    }

    @Transactional
    public Cats createCatFromDto(CatDto catDto) {
        Cats cat = dtoMapper.toCatEntity(catDto);

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
}