package com.project4.Dto;

import com.project4.models.Cats;
import com.project4.models.Owners;
import com.project4.repositories.CatRepository;
import com.project4.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;

    @Autowired
    public DtoMapper(OwnerRepository ownerRepository, CatRepository catRepository) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
    }

    public CatDto toCatDto(Cats cat) {
        if (cat == null) return null;

        List<Long> friendIds = cat.getFriends() != null
                ? cat.getFriends().stream()
                .map(Cats::getId)
                .toList()
                : null;

        return new CatDto(
                cat.getId(),
                cat.getName(),
                cat.getBirthDate(),
                cat.getBreed(),
                cat.getColor(),
                cat.getOwner() != null ? cat.getOwner().getId() : null,
                friendIds
        );
    }


    public Cats toCatEntity(CatDto catDto) {
        if (catDto == null) return null;

        Cats cat = new Cats();
        cat.setName(catDto.name());
        cat.setBirthDate(catDto.birthDate());
        cat.setBreed(catDto.breed());
        cat.setColor(catDto.color());

        if (catDto.ownerId() != null) {
            Optional<Owners> owner = ownerRepository.findById(catDto.ownerId());
            owner.ifPresent(cat::setOwner);
        }

        if (catDto.catFriendsId() != null) {
            List<Cats> friends = catDto.catFriendsId().stream()
                    .map(id -> catRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Friend cat not found with id: " + id)))
                    .toList();
            cat.setFriends(friends);
        }

        return cat;
    }

    public Cats updateCatEntity(Cats cat, CatDto catDto) {
        if (catDto == null || cat == null) return cat;

        if (catDto.name() != null) cat.setName(catDto.name());
        if (catDto.birthDate() != null) cat.setBirthDate(catDto.birthDate());
        if (catDto.breed() != null) cat.setBreed(catDto.breed());
        if (catDto.color() != null) cat.setColor(catDto.color());

        if (catDto.ownerId() != null) {
            Owners owner = ownerRepository.findById(catDto.ownerId())
                    
                    .orElseThrow(() -> new RuntimeException("Owner not found with id: " + catDto.ownerId()));
            cat.setOwner(owner);
        } else {
            cat.setOwner(null); 
        }

        if (catDto.catFriendsId() != null) {
            List<Cats> oldFriends = new ArrayList<>(cat.getFriends());

            List<Cats> newFriends = catDto.catFriendsId().stream()
                    .map(friendId -> catRepository.findById(friendId)
                            .orElseThrow(() -> new RuntimeException("Friend cat not found with id: " + friendId)))
                    .collect(Collectors.toList());

            for (Cats oldFriend : oldFriends) {
                if (!newFriends.contains(oldFriend)) {
                    oldFriend.getFriends().remove(cat);
                }
            }

            for (Cats newFriend : newFriends) {
                if (!newFriend.getFriends().contains(cat)) {
                    newFriend.getFriends().add(cat);
                }
            }

            cat.setFriends(newFriends);
        }
        return cat;
    }

    public OwnerDto toOwnerDto(Owners owner) {
        if (owner == null) return null;

        List<CatDto> catDtos = null;
        
        if (owner.getCats() != null) {
            catDtos = owner.getCats().stream()
                    .map(this::toCatDto) 
                    .collect(Collectors.toList());
        }

        return new OwnerDto(
                owner.getId(),
                owner.getName(),
                owner.getBirthDate(),
                catDtos 
        );
    }

    public Owners toOwnerEntity(OwnerDto ownerDto) {
        if (ownerDto == null) return null;
        Owners owner = new Owners();
        owner.setName(ownerDto.name());
        owner.setBirthDate(ownerDto.birthDate());

        return owner;
    }

    public Owners updateOwnerEntity(Owners owner, OwnerDto ownerDto) {
        if (ownerDto == null || owner == null) return owner;
        if (ownerDto.name() != null) owner.setName(ownerDto.name());
        if (ownerDto.birthDate() != null) owner.setBirthDate(ownerDto.birthDate());

        return owner;
    }
}