package com.project2.tests;

import com.project2.models.Owners;
import com.project2.repositories.OwnerRepository;
import com.project2.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OwnerTests {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    private Owners owner;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        owner = new Owners();
        owner.setId(1L);
        owner.setName("Артурчик");
        owner.setBirthDate(LocalDate.of(1990, 5, 10));
    }

    @Test
    public void testSaveOwner() {
        when(ownerRepository.save(owner)).thenReturn(owner);

        Owners savedOwner = ownerService.saveOwner(owner);

        assertNotNull(savedOwner);
        assertEquals(owner.getName(), savedOwner.getName());
        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    public void testGetOwnerById() {
        when(ownerRepository.getById(1L)).thenReturn(owner);

        Owners foundOwner = ownerService.getOwnerById(1L);
        assertNotNull(foundOwner);
        assertEquals(owner.getName(), foundOwner.getName());
        verify(ownerRepository, times(1)).getById(1L);
    }

    @Test
    public void testGetAllOwners() {
        List<Owners> owners = Arrays.asList(owner, new Owners());
        when(ownerRepository.getAll()).thenReturn(owners);

        List<Owners> allOwners = ownerService.getAllOwners();
        assertNotNull(allOwners);
        assertEquals(2, allOwners.size());
        verify(ownerRepository, times(1)).getAll();
    }

    @Test
    public void testUpdateOwner() {
        Owners updatedOwner = new Owners();
        updatedOwner.setId(1L);
        updatedOwner.setName("Алексей");
        updatedOwner.setBirthDate(LocalDate.of(1995, 8, 20));

        when(ownerRepository.update(updatedOwner)).thenReturn(updatedOwner);

        Owners resultOwner = ownerService.updateOwner(updatedOwner);

        assertNotNull(resultOwner);
        assertEquals("Алексей", resultOwner.getName());
        verify(ownerRepository, times(1)).update(updatedOwner);
    }

    @Test
    public void testDeleteOwner() {
        ownerService.deleteOwner(owner);
        verify(ownerRepository, times(1)).deleteByEntity(owner);
    }

    @Test
    public void testDeleteOwnerById() {
        ownerService.deleteOwnerById(1L);
        verify(ownerRepository, times(1)).deleteById(1L);
    }
}