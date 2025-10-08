package com.project3;

import com.project3.controllers.OwnerController;
import com.project3.Dto.CatDto;
import com.project3.Dto.DtoMapper;
import com.project3.Dto.OwnerDto;
import com.project3.models.Cats;
import com.project3.models.Owners;
import com.project3.services.OwnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
public class OwnerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @MockBean
    private DtoMapper dtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private OwnerDto createOwnerDto(Long id, String name, LocalDate birthDate, List<CatDto> cats) {
        return new OwnerDto(id, name, birthDate, cats != null ? cats : Collections.emptyList());
    }

    private Owners createOwnerEntity(Long id, String name, LocalDate birthDate, List<Cats> cats) {
        Owners owner = new Owners();
        owner.setId(id);
        owner.setName(name);
        owner.setBirthDate(birthDate);
        owner.setCats(cats != null ? cats : new ArrayList<>());
        return owner;
    }


    private CatDto createSimpleCatDto(Long id, String name) {
        return new CatDto(id, name, null, null, null, null, null);
    }

    private Cats createSimpleCatEntity(Long id, String name) {
        Cats cat = new Cats();
        cat.setId(id);
        cat.setName(name);
        return cat;
    }


    @Test
    void getOwnerById_shouldReturnOwner_whenFound() throws Exception {
        Long ownerId = 1L;

        List<Cats> ownerCats = Arrays.asList(createSimpleCatEntity(101L, "Котофей"), createSimpleCatEntity(102L, "Арбуз"));
        Owners mockOwner = createOwnerEntity(ownerId, "Николай", LocalDate.of(1990, 1, 1), ownerCats);

        List<CatDto> ownerCatDtos = Arrays.asList(createSimpleCatDto(101L, "Котофей"), createSimpleCatDto(102L, "Арбуз"));
        OwnerDto expectedDto = createOwnerDto(ownerId, "Николай", LocalDate.of(1990, 1, 1), ownerCatDtos);

        when(ownerService.getOwnerById(ownerId)).thenReturn(mockOwner);
        when(dtoMapper.toOwnerDto(mockOwner)).thenReturn(expectedDto);

        mockMvc.perform(get("/api/v1/owners/{id}", ownerId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(ownerId))
                .andExpect(jsonPath("$.name").value("Николай"))
                .andExpect(jsonPath("$.cats").isArray())
                .andExpect(jsonPath("$.cats.length()").value(2))
                .andExpect(jsonPath("$.cats[0].id").value(101L));

        verify(ownerService, times(1)).getOwnerById(ownerId);
        verify(dtoMapper, times(1)).toOwnerDto(mockOwner);
    }

    @Test
    void getOwnerById_shouldReturnNotFound_whenNotFound() throws Exception {
        Long ownerId = 99L;
        when(ownerService.getOwnerById(ownerId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/owners/{id}", ownerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(ownerService, times(1)).getOwnerById(ownerId);
        verify(dtoMapper, times(0)).toOwnerDto(any());
    }

    @Test
    void createOwner_shouldReturnCreatedOwner() throws Exception {

        OwnerDto incomingDto = createOwnerDto(null, "Жимбо", LocalDate.of(2000, 10, 20), null);

        Owners entityAfterMappingFromDto = createOwnerEntity(null, "Жимбо", LocalDate.of(2000, 10, 20), new ArrayList<>());

        Owners savedEntity = createOwnerEntity(5L, "Жимбо", LocalDate.of(2000, 10, 20), new ArrayList<>());

        OwnerDto expectedResponseDto = createOwnerDto(5L, "Жимбо", LocalDate.of(2000, 10, 20), Collections.emptyList());


        when(dtoMapper.toOwnerEntity(any(OwnerDto.class))).thenReturn(entityAfterMappingFromDto);

        when(ownerService.saveOwner(any(Owners.class))).thenReturn(savedEntity);

        when(dtoMapper.toOwnerDto(savedEntity)).thenReturn(expectedResponseDto);

        mockMvc.perform(post("/api/v1/owners")
                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(incomingDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.name").value("Жимбо"))
                .andExpect(jsonPath("$.birthDate").value("2000-10-20"))
                .andExpect(jsonPath("$.cats").isArray())
                .andExpect(jsonPath("$.cats").isEmpty());


        verify(dtoMapper, times(1)).toOwnerEntity(any(OwnerDto.class));
        verify(ownerService, times(1)).saveOwner(any(Owners.class));
        verify(dtoMapper, times(1)).toOwnerDto(savedEntity);
    }

    @Test
    void updateOwner_shouldReturnUpdatedOwner_whenFound() throws Exception {
        Long ownerId = 1L;

        Owners existingOwnerEntity = createOwnerEntity(ownerId, "Кизара", LocalDate.of(1990, 1, 1), new ArrayList<>());

        OwnerDto updateDto = createOwnerDto(null, "Человек", LocalDate.of(1990, 1, 1), null);

        Owners entityAfterMappingFromDto = createOwnerEntity(ownerId, "Человек", LocalDate.of(1990, 1, 1), new ArrayList<>());

        Owners savedUpdatedEntity = createOwnerEntity(ownerId, "Человек", LocalDate.of(1990, 1, 1), new ArrayList<>());


        when(ownerService.getOwnerById(ownerId)).thenReturn(existingOwnerEntity);

        when(dtoMapper.updateOwnerEntity(eq(existingOwnerEntity), any(OwnerDto.class))).thenReturn(entityAfterMappingFromDto);

        when(ownerService.updateOwner(any(Owners.class))).thenReturn(savedUpdatedEntity);

        OwnerDto expectedResponseDto = createOwnerDto(ownerId, "Человек", LocalDate.of(1990, 1, 1), Collections.emptyList());
        when(dtoMapper.toOwnerDto(savedUpdatedEntity)).thenReturn(expectedResponseDto);

        mockMvc.perform(put("/api/v1/owners/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(ownerId))
                .andExpect(jsonPath("$.name").value("Человек"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.cats").isArray());


        verify(ownerService, times(1)).getOwnerById(ownerId);

        verify(dtoMapper, times(1)).updateOwnerEntity(eq(existingOwnerEntity), any(OwnerDto.class));

        verify(ownerService, times(1)).updateOwner(eq(entityAfterMappingFromDto));

        verify(dtoMapper, times(1)).toOwnerDto(savedUpdatedEntity);
    }


    @Test
    void updateOwner_shouldReturnNotFound_whenOwnerNotFound() throws Exception {
        Long ownerId = 99L;

        OwnerDto updateDto = createOwnerDto(null, "Человек", LocalDate.of(1990, 1, 1), null);

        when(ownerService.getOwnerById(ownerId)).thenReturn(null);

        mockMvc.perform(put("/api/v1/owners/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        verify(ownerService, times(1)).getOwnerById(ownerId);
        verify(dtoMapper, times(0)).updateOwnerEntity(any(), any());
        verify(ownerService, times(0)).updateOwner(any());
        verify(dtoMapper, times(0)).toOwnerDto(any());
    }


    @Test
    void deleteOwner_shouldReturnNoContent() throws Exception {
        Long ownerId = 1L;

        doNothing().when(ownerService).deleteOwnerById(ownerId);

        mockMvc.perform(delete("/api/v1/owners/{id}", ownerId))
                .andExpect(status().isNoContent());

        verify(ownerService, times(1)).deleteOwnerById(ownerId);
    }
}
