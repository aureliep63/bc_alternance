package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.mapper.LieuxMapper;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.LieuxRepository;
import com.example.BC_alternance.service.GeocodingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LieuxServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger
            (LieuxServiceImplTest.class);
    @BeforeEach
    void init(TestInfo testInfo) {
        log.info("Début du test : {}", testInfo.getDisplayName());
    }
    @Mock
    private LieuxRepository lieuxRepository;
    @Mock
    private BorneRepository borneRepository;
    @Mock
    private LieuxMapper lieuxMapper;
    @Mock
    private GeocodingService geocodingService;
    @InjectMocks
    private LieuxServiceImpl lieuxService;
    private Lieux lieux;
    private LieuxDto lieuxDto;

    @BeforeEach
    void setup() {
        lieuxDto = new LieuxDto();
        lieuxDto.setId(null);
        lieuxDto.setAdresse("10 rue Test");
        lieuxDto.setVille("Paris");
        lieuxDto.setCodePostal("75000");
        lieuxDto.setBornesId(List.of(1L, 2L));

        lieux = new Lieux();
        lieux.setId(1L);
        lieux.setAdresse("10 rue Test");
        lieux.setVille("Paris");
        lieux.setCodePostal("75000");
        lieux.setBornes(new ArrayList<>());
    }

    @Test
    void testGetAllLieux() {
        List<Lieux> lieuxList = List.of(lieux);
        List<LieuxDto> dtoList = List.of(lieuxDto);

        when(lieuxRepository.findAll()).thenReturn(lieuxList);
        when(lieuxMapper.toDto(any(Lieux.class))).thenReturn(lieuxDto);

        List<LieuxDto> result = lieuxService.getAllLieux();

        assertThat(result).isEqualTo(dtoList);
        verify(lieuxRepository).findAll();
        verify(lieuxMapper).toDto(any(Lieux.class));
    }

    @Test
    void testGetLieuxById() {
        when(lieuxRepository.findById(1L)).thenReturn(Optional.of(lieux));
        when(lieuxMapper.toDto(lieux)).thenReturn(lieuxDto);

        LieuxDto result = lieuxService.getLieuxById(1L);

        assertThat(result).isEqualTo(lieuxDto);
        verify(lieuxRepository).findById(1L);
    }

    @Test
    void testGetLieuxByIdNotFound() {
        when(lieuxRepository.findById(1L)).thenReturn(Optional.empty());

        LieuxDto result = lieuxService.getLieuxById(1L);

        assertThat(result).isNull();
        verify(lieuxRepository).findById(1L);
    }

    @Test
    void testCreateLieuxSansLatitudeLongitude() {
        // DTO envoyé depuis le front (sans lat/long)
        LieuxDto dtoSansCoords = new LieuxDto();
        dtoSansCoords.setAdresse("10 rue Test");
        dtoSansCoords.setVille("Paris");
        dtoSansCoords.setCodePostal("75000");
        dtoSansCoords.setBornesId(List.of(1L, 2L));

        // Entité créée par le mapper (sans coords initialement)
        Lieux lieuxSansCoords = new Lieux();
        lieuxSansCoords.setAdresse("10 rue Test");
        lieuxSansCoords.setVille("Paris");
        lieuxSansCoords.setCodePostal("75000");
        lieuxSansCoords.setBornes(new ArrayList<>());

        // ✅ On construit la même adresse que dans le service
        String expectedFullAddress = "10 rue Test, Paris, 75000";

        // ✅ Mocks
        when(lieuxMapper.toEntity(any(LieuxDto.class))).thenReturn(lieuxSansCoords);
        when(geocodingService.geocodeAddress(eq(expectedFullAddress)))
                .thenReturn(new GeocodingService.LatLng(48.8566, 2.3522));
        when(borneRepository.findAllById(anyList())).thenReturn(new ArrayList<>());
        when(lieuxRepository.save(any(Lieux.class))).thenReturn(lieuxSansCoords);

        // ✅ Appel du service
        Lieux result = lieuxService.saveLieux(dtoSansCoords);

        // ✅ Assertions
        assertThat(result.getLatitude()).isEqualTo(48.8566);
        assertThat(result.getLongitude()).isEqualTo(2.3522);

        // ✅ Vérifications
        verify(lieuxMapper).toEntity(dtoSansCoords);
        verify(geocodingService).geocodeAddress(eq(expectedFullAddress));
        verify(lieuxRepository).save(lieuxSansCoords);
    }



    @Test
    void testDeleteLieux() {
        doNothing().when(lieuxRepository).deleteById(1L);

        lieuxService.deleteLieux(1L);

        verify(lieuxRepository).deleteById(1L);
    }


}
