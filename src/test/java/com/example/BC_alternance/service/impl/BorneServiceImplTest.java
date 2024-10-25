//package com.example.BC_alternance.service.impl;
//
//import com.example.BC_alternance.model.Borne;
//import com.example.BC_alternance.model.Lieux;
//import com.example.BC_alternance.model.Reservation;
//import com.example.BC_alternance.model.Utilisateur;
//import com.example.BC_alternance.repository.BorneRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.Month;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//class BorneServiceImplTest {
//    @Mock
//    BorneRepository borneRepository;
//    @InjectMocks
//    BorneServiceImpl borneServiceImpl;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllBornes() {
//        when(borneRepository.findAll()).thenReturn(List.of(new Borne()));
//
//        List<Borne> result = borneServiceImpl.getAllBornes();
//        Assertions.assertEquals(List.of(new Borne(Long.valueOf(1), "nom", 0f, true, "instruction", true, 0d, 0d, 0f, "photo", new Utilisateur(Long.valueOf(1), "nom", "prenom", "email", "motDePasse", "telephone", "role", LocalDate.of(2024, Month.OCTOBER, 15), "nomRue", "codePostal", "ville", List.of(null), List.of(new Reservation(Long.valueOf(1), LocalDate.of(2024, Month.OCTOBER, 15), LocalDate.of(2024, Month.OCTOBER, 15), LocalTime.of(11, 55, 5), LocalTime.of(11, 55, 5), null, null))), new Lieux(Long.valueOf(1), "adresse", "codePostal", "ville", List.of(null)))), result);
//    }
//
//    @Test
//    void testGetBorneById() {
//        when(borneRepository.findById(any(Long.class))).thenReturn(null);
//
//        Borne result = borneServiceImpl.getBorneById(Long.valueOf(1));
//        Assertions.assertEquals(new Borne(Long.valueOf(1), "nom", 0f, true, "instruction", true, 0d, 0d, 0f, "photo", new Utilisateur(Long.valueOf(1), "nom", "prenom", "email", "motDePasse", "telephone", "role", LocalDate.of(2024, Month.OCTOBER, 15), "nomRue", "codePostal", "ville", List.of(null), List.of(new Reservation(Long.valueOf(1), LocalDate.of(2024, Month.OCTOBER, 15), LocalDate.of(2024, Month.OCTOBER, 15), LocalTime.of(11, 55, 5), LocalTime.of(11, 55, 5), null, null))), new Lieux(Long.valueOf(1), "adresse", "codePostal", "ville", List.of(null))), result);
//    }
//
//    @Test
//    void testSaveBorne() {
//        when(borneRepository.save(any(Borne.class))).thenReturn(new Borne());
//
//        Borne result = borneServiceImpl.saveBorne(new Borne(Long.valueOf(1), "nom", 0f, true, "instruction", true, 0d, 0d, 0f, "photo", new Utilisateur(Long.valueOf(1), "nom", "prenom", "email", "motDePasse", "telephone", "role", LocalDate.of(2024, Month.OCTOBER, 15), "nomRue", "codePostal", "ville", List.of(null), List.of(new Reservation(Long.valueOf(1), LocalDate.of(2024, Month.OCTOBER, 15), LocalDate.of(2024, Month.OCTOBER, 15), LocalTime.of(11, 55, 5), LocalTime.of(11, 55, 5), null, null))), new Lieux(Long.valueOf(1), "adresse", "codePostal", "ville", List.of(null))));
//        Assertions.assertEquals(new Borne(Long.valueOf(1), "nom", 0f, true, "instruction", true, 0d, 0d, 0f, "photo", new Utilisateur(Long.valueOf(1), "nom", "prenom", "email", "motDePasse", "telephone", "role", LocalDate.of(2024, Month.OCTOBER, 15), "nomRue", "codePostal", "ville", List.of(null), List.of(new Reservation(Long.valueOf(1), LocalDate.of(2024, Month.OCTOBER, 15), LocalDate.of(2024, Month.OCTOBER, 15), LocalTime.of(11, 55, 5), LocalTime.of(11, 55, 5), null, null))), new Lieux(Long.valueOf(1), "adresse", "codePostal", "ville", List.of(null))), result);
//    }
//
//    @Test
//    void testDeleteBorne() {
//        borneServiceImpl.deleteBorne(Long.valueOf(1));
//        verify(borneRepository).deleteById(any(Long.class));
//    }
//}
//
////Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme