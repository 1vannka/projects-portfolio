package com.project4.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project4.config.SecurityConfig;
import com.project4.controllers.CatController;
import com.project4.Dto.CatDto;
import com.project4.Dto.DtoMapper;
import com.project4.models.Cats;
import com.project4.models.Color;
import com.project4.models.Owners; 
import com.project4.services.CatService;
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

@WebMvcTest(CatController.class)
@Import(SecurityConfig.class)
public class CatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "catService")
    private CatService catService;

    @MockBean
    private DtoMapper dtoMapper;

    private CatDto sampleCatDto;
    private Cats sampleCatEntity;
    private Owners sampleOwner;

    @BeforeEach
    void setUp() {
        sampleOwner = new Owners();
        sampleOwner.setId(10L);
        sampleOwner.setName("Алексей");

        sampleCatEntity = createCatEntity(1L, "Шарик", sampleOwner, Collections.emptyList());
        sampleCatDto = createCatDto(1L, "Шарик", 10L, Collections.emptyList());
    }

    private CatDto createCatDto(Long id, String name, Long ownerId, List<Long> catFriendsId) {
        return new CatDto(id, name, LocalDate.now(), "Дворняга", Color.BLACK, ownerId, catFriendsId);
    }

    private Cats createCatEntity(Long id, String name, Owners owner, List<Cats> catFriends) {
        Cats cat = new Cats();
        cat.setId(id);
        cat.setName(name);
        cat.setBirthDate(LocalDate.now());
        cat.setBreed("Дворняга");
        cat.setColor(Color.BLACK);
        cat.setOwner(owner);
        cat.setFriends(catFriends != null ? catFriends : new ArrayList<>());
        return cat;
    }

    @Test
    void getCatById_asUser_shouldReturnCat_whenFound() throws Exception {
        when(catService.getCatById(1L)).thenReturn(sampleCatEntity);
        when(dtoMapper.toCatDto(sampleCatEntity)).thenReturn(sampleCatDto);

        mockMvc.perform(get("/api/v1/cats/{id}", 1L)
                        .with(user("testuser").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Шарик"))
                .andExpect(jsonPath("$.ownerId").value(10L));

        verify(catService).getCatById(1L);
        verify(dtoMapper).toCatDto(sampleCatEntity);
    }

    @Test
    void createCat_asUser_shouldReturnCreatedCat() throws Exception {
        
        CatDto catToCreateDto = createCatDto(null, "Фараон", 10L, Collections.emptyList());
        Cats savedEntity = createCatEntity(5L, "Фараон", sampleOwner, Collections.emptyList());
        CatDto expectedResponseDto = createCatDto(5L, "Фараон", 10L, Collections.emptyList());

        when(catService.createCatFromDto(any(CatDto.class), any(Authentication.class))).thenReturn(savedEntity);
        when(dtoMapper.toCatDto(savedEntity)).thenReturn(expectedResponseDto);

        mockMvc.perform(post("/api/v1/cats")
                        .with(user("testuser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(catToCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.name").value("Фараон"));

        verify(catService).createCatFromDto(any(CatDto.class), any(Authentication.class));
        verify(dtoMapper).toCatDto(savedEntity);
    }

    @Test
    void updateCatAsOwner_shouldReturnOk() throws Exception {
        Long id = 1L;
        CatDto updatedCatDto = createCatDto(id, "Name", 10L, null);
        Cats updatedEntity = createCatEntity(id, "Name", sampleOwner, Collections.emptyList());

        when(catService.IsOwner(eq(id), any(Authentication.class))).thenReturn(true);
        when(catService.updateCatFromDto(eq(id), any(CatDto.class))).thenReturn(updatedEntity);
        when(dtoMapper.toCatDto(updatedEntity)).thenReturn(updatedCatDto);


        mockMvc.perform(put("/api/v1/cats/{id}", id)
                        .with(user("testuser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCatDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name"));
    }

    @Test
    void updateCatAsNonOwnerUser_shouldReturnForbidden() throws Exception {
        Long catIdToUpdate = 1L;
        CatDto updatedCatDto = createCatDto(catIdToUpdate, "Forbidden Name", 10L, null);

        when(catService.IsOwner(eq(catIdToUpdate), any(Authentication.class))).thenReturn(false);

        mockMvc.perform(put("/api/v1/cats/{id}", catIdToUpdate)
                        .with(user("otheruser").roles("USER"))
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCatDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteCat_asNonOwnerUser_shouldReturnForbidden() throws Exception {
        Long catId = 1L;
        when(catService.IsOwner(eq(catId), any(Authentication.class))).thenReturn(false);

        mockMvc.perform(delete("/api/v1/cats/{id}", catId)
                        .with(user("otheruser").roles("USER"))
                        )
                .andExpect(status().isForbidden());
        verify(catService, never()).deleteCatById(catId);
    }
}