package com.example.BC_alternance.controller;

import com.example.BC_alternance.controller.rest.LieuxRestController;
import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.mapper.LieuxMapper;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.service.LieuxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LieuxRestControllerTest {
// Test la classe isolé en utilisant Mockito pour mocker les dépendances
// Aucun contexte Spring Boot n’est chargé (pas de serveur, pas de configuration Web)

    private static final Logger log = LoggerFactory.getLogger(LieuxRestControllerTest.class);

    @BeforeEach
    void init(TestInfo testInfo) {
        log.info("➡️ Lancement du test : {}", testInfo.getDisplayName());
    }
    @Mock
    private LieuxService lieuxService;

    @Mock
    private LieuxMapper lieuxMapper;

    @InjectMocks
    private LieuxRestController lieuxRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private LieuxDto lieuxDto;
    private Lieux lieux;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(lieuxRestController).build();
        objectMapper = new ObjectMapper();

        lieuxDto = new LieuxDto();
        lieuxDto.setId(1L);
        lieuxDto.setAdresse("10 rue Test");
        lieuxDto.setVille("Paris");
        lieuxDto.setCodePostal("75000");

        lieux = new Lieux();
        lieux.setId(1L);
        lieux.setAdresse("10 rue Test");
        lieux.setVille("Paris");
        lieux.setCodePostal("75000");
    }

    // Test GET /lieux
    @Test
    void testGetAllLieux() throws Exception {
        when(lieuxService.getAllLieux()).thenReturn(List.of(lieuxDto));

        mockMvc.perform(get("/lieux"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].adresse").value("10 rue Test"));

        verify(lieuxService).getAllLieux();
    }

    // Test GET /lieux/{id}
    @Test
    void testGetLieuxByIdFound() throws Exception {
        when(lieuxService.getLieuxById(1L)).thenReturn(lieuxDto);

        mockMvc.perform(get("/lieux/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ville").value("Paris"));

        verify(lieuxService).getLieuxById(1L);
    }

    @Test
    void testGetLieuxByIdNotFound() throws Exception {
        when(lieuxService.getLieuxById(1L)).thenReturn(null);

        mockMvc.perform(get("/lieux/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // null retourne vide

        verify(lieuxService).getLieuxById(1L);
    }

    // Test POST /lieux
    @Test
    void testSaveLieux() throws Exception {
        when(lieuxService.saveLieux(any(LieuxDto.class))).thenReturn(lieux);
        when(lieuxMapper.toDto(lieux)).thenReturn(lieuxDto);

        mockMvc.perform(post("/lieux")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lieuxDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codePostal").value("75000"));

        verify(lieuxService).saveLieux(any(LieuxDto.class));
        verify(lieuxMapper).toDto(lieux);
    }

    // Test PUT /lieux/{id}
    @Test
    void testUpdateLieux() throws Exception {
        when(lieuxService.saveLieux(any(LieuxDto.class))).thenReturn(lieux);
        when(lieuxMapper.toDto(lieux)).thenReturn(lieuxDto);

        mockMvc.perform(put("/lieux/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lieuxDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adresse").value("10 rue Test"));

        verify(lieuxService).saveLieux(any(LieuxDto.class));
        verify(lieuxMapper).toDto(lieux);
    }

    // Test DELETE /lieux/{id}
    @Test
    void testDeleteLieux() throws Exception {
        doNothing().when(lieuxService).deleteLieux(1L);

        mockMvc.perform(delete("/lieux/1"))
                .andExpect(status().isNoContent());

        verify(lieuxService).deleteLieux(1L);
    }

    // Test POST /lieux/update-coords
    @Test
    void testUpdateCoords() throws Exception {
        doNothing().when(lieuxService).updateCoordinatesForAllLieuxWithoutCoords();

        mockMvc.perform(post("/lieux/update-coords"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mise à jour des coordonnées lancée"));

        verify(lieuxService).updateCoordinatesForAllLieuxWithoutCoords();
    }
}
