package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.mapper.BorneMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.repository.*;
import com.example.BC_alternance.service.LieuxService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorneServiceImplTest {

    @Mock
    private BorneRepository borneRepository;
    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private BorneMapper borneMapper;

    @InjectMocks
    private BorneServiceImpl borneService;

    private Borne borne;
    private BorneDto borneDto;
    private static final Logger log = LoggerFactory.getLogger(BorneServiceImplTest.class);

    @BeforeEach
    void init(TestInfo testInfo) {
        log.info("Début du test : {}", testInfo.getDisplayName());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        borne = new Borne();
        borne.setId(1L);
        borne.setNom("BorneTest");
        borne.setPrix(100);

        borneDto = new BorneDto();
        borneDto.setId(1L);
        borneDto.setNom("BorneTest");
        borneDto.setPrix(100);
    }

    @Test
    void testGetAllBornes() {
        when(borneRepository.findAll()).thenReturn(Arrays.asList(borne));
        when(borneMapper.toDto(borne)).thenReturn(borneDto);

        List<BorneDto> result = borneService.getAllBornes();

        assertEquals(1, result.size());
        assertEquals("BorneTest", result.get(0).getNom());
        verify(borneRepository, times(1)).findAll();
    }

    @Test
    void testGetBorneById_Found() {
        when(borneRepository.findById(1L)).thenReturn(Optional.of(borne));
        when(borneMapper.toDto(borne)).thenReturn(borneDto);

        BorneDto result = borneService.getBorneById(1L);

        assertNotNull(result);
        assertEquals("BorneTest", result.getNom());
        verify(borneRepository).findById(1L);
    }

    @Test
    void testGetBorneById_NotFound() {
        when(borneRepository.findById(1L)).thenReturn(Optional.empty());

        BorneDto result = borneService.getBorneById(1L);

        assertNull(result);
        verify(borneRepository).findById(1L);
    }

    @Test
    void testSaveBorne_CreateNew() {
        borneDto.setId(null);
        when(borneMapper.toEntity(borneDto)).thenReturn(borne);
        when(utilisateurRepository.findById(anyLong())).thenThrow(new EntityNotFoundException("Utilisateur non trouvé"));

        assertThrows(EntityNotFoundException.class, () -> borneService.saveBorne(borneDto));
    }

    @Test
    void testDeleteBorne() {
        doNothing().when(borneRepository).deleteById(1L);

        borneService.deleteBorne(1L);

        verify(borneRepository).deleteById(1L);
    }
}
