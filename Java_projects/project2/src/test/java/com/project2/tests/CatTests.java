package com.project2.tests;

import com.project2.models.Cats;
import com.project2.models.Color;
import com.project2.models.Owners;
import com.project2.repositories.CatRepository;
import com.project2.services.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CatTests {

    @Mock
    private CatRepository catRepository;

    @InjectMocks
    private CatService catService;

    private Cats cat;
    private Owners owner;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cat = new Cats();
        cat.setId(1L);
        cat.setName("Бобик");
        cat.setBirthDate(LocalDate.of(2023, 1, 15));
        cat.setBreed("Дворняга");
        cat.setColor(Color.WHITE);

        owner = new Owners();
        owner.setId(1L);
        owner.setName("Аристотель");
        owner.setBirthDate(LocalDate.of(1990, 5, 10));

        cat.setOwner(owner);
        cat.setFriends(Collections.emptyList());
    }

    @Test
    public void testSaveCat() {
        when(catRepository.save(cat)).thenReturn(cat);

        Cats savedCat = catService.saveCat(cat);

        assertNotNull(savedCat);
        assertEquals(cat.getName(), savedCat.getName());
        verify(catRepository, times(1)).save(cat);
    }

    @Test
    public void testGetCatById() {
        when(catRepository.getById(1L)).thenReturn(cat);

        Cats foundCat = catService.getCatById(1L);
        assertNotNull(foundCat);
        assertEquals(cat.getName(), foundCat.getName());
        verify(catRepository, times(1)).getById(1L);
    }

    @Test
    public void testGetAllCats() {
        List<Cats> cats = Arrays.asList(cat, new Cats());
        when(catRepository.getAll()).thenReturn(cats);

        List<Cats> allCats = catService.getAllCats();
        assertNotNull(allCats);
        assertEquals(2, allCats.size());
        verify(catRepository, times(1)).getAll();
    }

    @Test
    public void testUpdateCat() {
        Cats updatedCat = new Cats();
        updatedCat.setId(1L);
        updatedCat.setName("Бобик Второй");
        updatedCat.setBirthDate(LocalDate.of(2023, 1, 15));
        updatedCat.setBreed("МейнКун");
        updatedCat.setColor(Color.WHITE);
        updatedCat.setOwner(owner);
        updatedCat.setFriends(Collections.emptyList());

        when(catRepository.update(updatedCat)).thenReturn(updatedCat);

        Cats resultCat = catService.updateCat(updatedCat);

        assertNotNull(resultCat);
        assertEquals("Бобик Второй", resultCat.getName());
        verify(catRepository, times(1)).update(updatedCat);
    }

    @Test
    public void testDeleteCat() {
        catService.deleteCat(cat);
        verify(catRepository, times(1)).deleteByEntity(cat);
    }

    @Test
    public void testDeleteCatById() {
        catService.deleteCatById(1L);
        verify(catRepository, times(1)).deleteById(1L);
    }
}