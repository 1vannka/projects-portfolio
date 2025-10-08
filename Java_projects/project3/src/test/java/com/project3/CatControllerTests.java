package com.project3;

import com.project3.controllers.CatController;
import com.project3.Dto.CatDto;
import com.project3.Dto.DtoMapper; 
import com.project3.models.Cats;
import com.project3.models.Color;
import com.project3.services.CatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc; 
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders; 
import org.springframework.test.web.servlet.result.MockMvcResultMatchers; 

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(CatController.class)
public class CatControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    private CatService catService;


    @MockBean
    private DtoMapper dtoMapper;

    private CatDto createCatDto(Long id, String name, Long ownerId, List<Long> catFriendsId) {
        return new CatDto(id, name, LocalDate.now(), "Дворняга", Color.BLACK, ownerId, catFriendsId);
    }

    private Cats createCatEntity(Long id, String name, Long ownerId, List<Cats> catFriends) {
        Cats cat = new Cats();
        cat.setId(id);
        cat.setName(name);
        cat.setBirthDate(LocalDate.now());
        cat.setBreed("Дворняга");
        cat.setColor(Color.BLACK);
        cat.setFriends(catFriends != null ? catFriends : new ArrayList<>());
        return cat;
    }

    @Test
    void getCatById_shouldReturnCat_whenFound() throws Exception {
        Long catId = 1L;
        Cats mockCat = createCatEntity(catId, "Шарик", 10L, Collections.emptyList());
        CatDto expectedDto = createCatDto(catId, "Шарик", 10L, Collections.emptyList());

        when(catService.getCatById(catId)).thenReturn(mockCat);
        when(dtoMapper.toCatDto(mockCat)).thenReturn(expectedDto); 

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats/{id}", catId)
                        .accept(MediaType.APPLICATION_JSON)) 
                
                .andExpect(MockMvcResultMatchers.status().isOk())
                
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(catId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Шарик"))
                
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(10L));

        verify(catService, times(1)).getCatById(catId);
        verify(dtoMapper, times(1)).toCatDto(mockCat); 
    }

    @Test
    void getCatById_shouldReturnNotFound_whenNotFound() throws Exception {
        Long catId = 99L;
        when(catService.getCatById(catId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats/{id}", catId)
                        .accept(MediaType.APPLICATION_JSON))
                
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(catService, times(1)).getCatById(catId);
        verify(dtoMapper, times(0)).toCatDto(any()); 
    }

    @Test
    void getAllCats_shouldReturnPageOfCats() throws Exception {
        
        Cats cat1 = createCatEntity(1L, "Шарик", 10L, Collections.emptyList());
        Cats cat2 = createCatEntity(2L, "Кубик", 10L, Collections.emptyList());
        CatDto catDto1 = createCatDto(1L, "Шарик", 10L, Collections.emptyList());
        CatDto catDto2 = createCatDto(2L, "Кубик", 10L, Collections.emptyList());

        Page<Cats> catsPage = new PageImpl<>(List.of(cat1, cat2));

        when(catService.getAllCatsPaginated(any(Pageable.class))).thenReturn(catsPage);

        when(dtoMapper.toCatDto(cat1)).thenReturn(catDto1);
        when(dtoMapper.toCatDto(cat2)).thenReturn(catDto2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2L)); 

        verify(catService, times(1)).getAllCatsPaginated(any(Pageable.class));
        
        verify(dtoMapper, times(1)).toCatDto(cat1);
        verify(dtoMapper, times(1)).toCatDto(cat2);
    }

    @Test
    void createCat_shouldReturnCreatedCat() throws Exception {
        Cats savedEntity = createCatEntity(5L, "Фараон", 10L, Collections.emptyList());
        CatDto expectedResponseDto = createCatDto(5L, "Фараон", 10L, Collections.emptyList());

        when(catService.createCatFromDto(any(CatDto.class))).thenReturn(savedEntity);
        when(dtoMapper.toCatDto(any(Cats.class))).thenReturn(expectedResponseDto);
        String a = String.valueOf(LocalDate.now());

        String incomingJson = "{\"name\":\"Фараон\", \"birthDate\":\"" + a + "\", \"breed\":\"Дворняга\", \"color\":\"BLACK\", \"ownerId\":10}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cats")
                        .contentType(MediaType.APPLICATION_JSON) 
                        .content(incomingJson)) 
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Фараон"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(a))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value("Дворняга"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("BLACK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(10L));
        verify(catService, times(1)).createCatFromDto(any(CatDto.class));
        verify(dtoMapper, times(1)).toCatDto(any(Cats.class));
    }

    @Test
    void deleteCat_shouldReturnNoContent() throws Exception {
        Long catId = 1L;
        doNothing().when(catService).deleteCatById(catId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cats/{id}", catId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(catService, times(1)).deleteCatById(catId);
    }
}
