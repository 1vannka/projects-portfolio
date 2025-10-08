package com.project4.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project4.config.SecurityConfig;
import com.project4.controllers.OwnerController;
import com.project4.Dto.CatDto;
import com.project4.Dto.DtoMapper;
import com.project4.Dto.OwnerDto;
import com.project4.models.Cats;
import com.project4.models.Color;
import com.project4.models.Owners;
import com.project4.models.Users; 
import com.project4.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
@Import(SecurityConfig.class)
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "ownerService")
    private OwnerService ownerService;

    @MockBean
    private DtoMapper dtoMapper;

    private OwnerDto sampleOwnerDto;
    private Owners sampleOwnerEntity;
    private Users sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new Users();
        sampleUser.setId(1L); 
        sampleUser.setUsername("testowner");

        sampleOwnerEntity = createOwnerEntity(1L, "Николай", LocalDate.of(1990, 1, 1), new ArrayList<>(), sampleUser);
        sampleOwnerDto = createOwnerDto(1L, "Николай", LocalDate.of(1990, 1, 1), Collections.emptyList());
    }

    private OwnerDto createOwnerDto(Long id, String name, LocalDate birthDate, List<CatDto> cats) {
        return new OwnerDto(id, name, birthDate, cats != null ? cats : Collections.emptyList());
    }

    private Owners createOwnerEntity(Long id, String name, LocalDate birthDate, List<Cats> cats, Users user) {
        Owners owner = new Owners();
        owner.setId(id); 
        owner.setName(name);
        owner.setBirthDate(birthDate);
        owner.setCats(cats != null ? cats : new ArrayList<>());
        owner.setUser(user);
        return owner;
    }

    private CatDto createSimpleCatDto(Long id, String name) {
        return new CatDto(id, name, LocalDate.now(), "Test Breed", Color.GREY, 1L, Collections.emptyList());
    }

    @Test
    void getOwnerById_asUser_shouldReturnOwner_whenFound() throws Exception {
        when(ownerService.getOwnerById(1L)).thenReturn(sampleOwnerEntity);
        when(dtoMapper.toOwnerDto(sampleOwnerEntity)).thenReturn(sampleOwnerDto);

        mockMvc.perform(get("/api/v1/owners/{id}", 1L)
                        .with(user("testuser").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Николай"));

        verify(ownerService).getOwnerById(1L);
        verify(dtoMapper).toOwnerDto(sampleOwnerEntity);
    }

    @Test
    void updateOwner_asSelf_shouldReturnOk() throws Exception {
        
        Long ownerIdToUpdate = 1L; 
        OwnerDto updateDto = createOwnerDto(null, "Человек", LocalDate.of(1990, 1, 1), null);

        Owners existingOwner = sampleOwnerEntity; 
        Owners entityAfterUpdateMapping = new Owners(); entityAfterUpdateMapping.setId(ownerIdToUpdate); entityAfterUpdateMapping.setName("Человек");
        Owners savedUpdatedEntity = new Owners(); savedUpdatedEntity.setId(ownerIdToUpdate); savedUpdatedEntity.setName("Человек");
        OwnerDto expectedResponseDto = createOwnerDto(ownerIdToUpdate, "Человек", LocalDate.of(1990,1,1), Collections.emptyList());


        when(ownerService.IsOwner(eq(ownerIdToUpdate), any(Authentication.class))).thenReturn(true);
        when(ownerService.getOwnerById(ownerIdToUpdate)).thenReturn(existingOwner);
        when(dtoMapper.updateOwnerEntity(eq(existingOwner), any(OwnerDto.class))).thenReturn(entityAfterUpdateMapping);
        when(ownerService.updateOwner(any(Owners.class))).thenReturn(savedUpdatedEntity);
        when(dtoMapper.toOwnerDto(savedUpdatedEntity)).thenReturn(expectedResponseDto);


        mockMvc.perform(put("/api/v1/owners/{id}", ownerIdToUpdate)
                        .with(user("testowner").roles("USER")) 
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Человек"));
    }

    @Test
    void updateOwner_asAdmin_shouldReturnOk() throws Exception {
        Long ownerIdToUpdate = 2L; 
        OwnerDto updateDto = createOwnerDto(null, "AdminUpdated", LocalDate.of(1992, 2, 2), null);

        Owners existingOwner = new Owners(); existingOwner.setId(ownerIdToUpdate);
        Owners entityAfterUpdateMapping = new Owners(); entityAfterUpdateMapping.setId(ownerIdToUpdate); entityAfterUpdateMapping.setName("AdminUpdated");
        Owners savedUpdatedEntity = new Owners(); savedUpdatedEntity.setId(ownerIdToUpdate); savedUpdatedEntity.setName("AdminUpdated");
        OwnerDto expectedResponseDto = createOwnerDto(ownerIdToUpdate, "AdminUpdated", LocalDate.of(1992,2,2), Collections.emptyList());


        when(ownerService.IsOwner(eq(ownerIdToUpdate), any(Authentication.class))).thenReturn(false); 
        when(ownerService.getOwnerById(ownerIdToUpdate)).thenReturn(existingOwner);
        when(dtoMapper.updateOwnerEntity(eq(existingOwner), any(OwnerDto.class))).thenReturn(entityAfterUpdateMapping);
        when(ownerService.updateOwner(any(Owners.class))).thenReturn(savedUpdatedEntity);
        when(dtoMapper.toOwnerDto(savedUpdatedEntity)).thenReturn(expectedResponseDto);

        mockMvc.perform(put("/api/v1/owners/{id}", ownerIdToUpdate)
                        .with(user("admin").roles("ADMIN"))
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AdminUpdated"));
    }

    @Test
    void deleteOwner_asAdmin_shouldReturnNoContent() throws Exception {

        Long ownerId = 1L;
        when(ownerService.IsOwner(eq(ownerId), any(Authentication.class))).thenReturn(false); 
        doNothing().when(ownerService).deleteOwnerById(ownerId);

        mockMvc.perform(delete("/api/v1/owners/{id}", ownerId)
                        .with(user("admin").roles("ADMIN"))
                        )
                .andExpect(status().isNoContent());
        verify(ownerService).deleteOwnerById(ownerId);
    }
}