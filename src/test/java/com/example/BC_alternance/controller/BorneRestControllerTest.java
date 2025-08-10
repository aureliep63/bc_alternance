package com.example.BC_alternance.controller;


import com.example.BC_alternance.controller.rest.BorneRestController;
import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.filters.JWTTokenFilter;
import com.example.BC_alternance.mapper.BorneMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.service.BorneService;
import com.example.BC_alternance.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorneRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class BorneRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BorneService borneService;
    @MockitoBean
    private BorneMapper borneMapper;
    @MockitoBean
    private JWTTokenFilter jwtTokenFilter;
    @MockitoBean
    private StorageService storageService;

    private static final Logger log = LoggerFactory.getLogger
            (BorneRestControllerTest.class);
    @BeforeEach
    void init(TestInfo testInfo) {
        log.info("Début du test : {}",
                testInfo.getDisplayName());
    }
    @Test
    void testGetAllBornes() throws Exception {
        BorneDto dto = new BorneDto();
        dto.setId(1L);
        dto.setNom("BorneTest");
        dto.setPrix(120);

        Mockito.when(borneService.getAllBornes()).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/bornes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom", is("BorneTest")));
    }

    @Test
    void testGetBorneById() throws Exception {
        BorneDto dto = new BorneDto();
        dto.setId(1L);
        dto.setNom("BorneTest");

        Mockito.when(borneService.getBorneById(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/bornes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", is("BorneTest")));
    }

    @Test
    void testGetBorneById_NotFound() throws Exception {
        Mockito.when(borneService.getBorneById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/bornes/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testCreateBorneWithLieuxAndFile() throws Exception {

        // Création des DTO
        LieuxDto lieuxDto = new LieuxDto();
        lieuxDto.setId(10L);
        lieuxDto.setAdresse("222 boulevard Gustave Flaubert");
        lieuxDto.setVille("Clermont-Ferrand");
        lieuxDto.setCodePostal("63000");

        BorneDto borneDto = new BorneDto();
        borneDto.setNom("BorneTest");
        borneDto.setPrix(5);
        borneDto.setPuissance(4);
        borneDto.setLieuId(lieuxDto.getId());
        borneDto.setUtilisateurId(1L);

        // DTO que le service va retourner après la sauvegarde
        Borne savedBorne = new Borne();
        savedBorne.setId(1L);
        savedBorne.setNom(borneDto.getNom());

        BorneDto savedDto = new BorneDto();
        savedDto.setId(1L);
        savedDto.setNom(borneDto.getNom());

        // Mock du service pour retourner une entité Borne
        Mockito.when(borneService.saveBorne(Mockito.any(BorneDto.class)))
                .thenReturn(savedBorne);
        Mockito.when(borneMapper.toDto(Mockito.any(Borne.class)))
                .thenReturn(savedDto);

        // Mock du StorageService pour retourner un nom de fichier
        Mockito.when(storageService.store(any(MultipartFile.class))).thenReturn("unique-filename.jpg");

        // Conversion JSON du DTO en string
        ObjectMapper mapper = new ObjectMapper();
        String dtoJson = mapper.writeValueAsString(borneDto);

        // Création d’un MockMultipartFile (fichier factice)
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "FakeImage".getBytes()
        );

        // Exécution du POST multipart/form-data
        mockMvc.perform(multipart("/bornes/user/bornes")
                        .file(file)
                        .param("borneDto", dtoJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nom").value("BorneTest"));

        verify(borneService).saveBorne(Mockito.any(BorneDto.class));
        verify(storageService).store(any(MultipartFile.class));
    }
}
