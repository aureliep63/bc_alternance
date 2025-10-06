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

@ExtendWith(MockitoExtension.class) //active mockito
class LieuxServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger
            (LieuxServiceImplTest.class);

    @Mock // Créé objet pour simuler les dep
    private LieuxRepository lieuxRepository;
    @Mock
    private BorneRepository borneRepository;
    @Mock
    private LieuxMapper lieuxMapper;
    @Mock
    private GeocodingService geocodingService;

    @InjectMocks // créé une instance de LieuServImpl et injecte les mocks
    private LieuxServiceImpl lieuxService;

    // servir pour données de test
    private Lieux lieux;
    private LieuxDto lieuxDto;

    @BeforeEach // qui va s'exécuter avant chaque test
    void init(TestInfo testInfo) {
        log.info("Début du test : {}", testInfo.getDisplayName());
    }
    @BeforeEach
    void setup() { // initialise objet de test
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
    void testGetAllLieux() { // renvoie la liste des DTO qui corresp aux entités
        List<Lieux> lieuxList = List.of(lieux);
        List<LieuxDto> dtoList = List.of(lieuxDto);

        //simule comportement du repo et du mapper
        when(lieuxRepository.findAll()).thenReturn(lieuxList); // retourne une liste de lieu
        when(lieuxMapper.toDto(any(Lieux.class))).thenReturn(lieuxDto); //les tranf en DTO

        List<LieuxDto> result = lieuxService.getAllLieux(); // appel la méthode du service

        assertThat(result).isEqualTo(dtoList); // verif que le résultat est égal à la liste de DTO
        verify(lieuxRepository).findAll(); // verif que findAll du repo a été appelé
        verify(lieuxMapper).toDto(any(Lieux.class)); // verif que toDto du mapper a été appelé et trans en dto
    }

    @Test
    void testGetLieuxById() { // vérifie que le service renvoi le bon dto
        when(lieuxRepository.findById(1L)).thenReturn(Optional.of(lieux)); // simule le resultat
        when(lieuxMapper.toDto(lieux)).thenReturn(lieuxDto); // simule le comportement du mapper

        LieuxDto result = lieuxService.getLieuxById(1L); // test la vrai méthode

        assertThat(result).isEqualTo(lieuxDto); // vérifie que le résultat = objet lieudto
        verify(lieuxRepository).findById(1L); // verif que findById du repo a été appelé
    }

    @Test
    void testGetLieuxByIdNotFound() { // retourne null si existe pas
        when(lieuxRepository.findById(1L)).thenReturn(Optional.empty());

        LieuxDto result = lieuxService.getLieuxById(1L);

        assertThat(result).isNull();
        verify(lieuxRepository).findById(1L);
    }

    @Test
    void testCreateLieuxSansLatitudeLongitude() { // simuler la creation d'un lieu sans coordonnées
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

        //  On construit la même adresse que dans le service
        String expectedFullAddress = "10 rue Test, Paris, 75000";

        //  Mocks
        when(lieuxMapper.toEntity(any(LieuxDto.class))).thenReturn(lieuxSansCoords);
        when(geocodingService.geocodeAddress(eq(expectedFullAddress)))
                .thenReturn(new GeocodingService.LatLng(48.8566, 2.3522));
        when(borneRepository.findAllById(anyList())).thenReturn(new ArrayList<>());
        when(lieuxRepository.save(any(Lieux.class))).thenReturn(lieuxSansCoords);

        // Appel du service
        Lieux result = lieuxService.saveLieux(dtoSansCoords);

        //  Assertions
        assertThat(result.getLatitude()).isEqualTo(48.8566);
        assertThat(result.getLongitude()).isEqualTo(2.3522);

        //  Vérifications
        verify(lieuxMapper).toEntity(dtoSansCoords);
        verify(geocodingService).geocodeAddress(eq(expectedFullAddress));
        verify(lieuxRepository).save(lieuxSansCoords);
    }



    @Test
    void testDeleteLieux() { // vérifie que le service supprime le lieu avec le bon id
        doNothing().when(lieuxRepository).deleteById(1L);

        lieuxService.deleteLieux(1L);

        verify(lieuxRepository).deleteById(1L);
    }


}
