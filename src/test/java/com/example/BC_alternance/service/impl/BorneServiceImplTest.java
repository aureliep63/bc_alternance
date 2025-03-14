//package com.example.BC_alternance.service.impl;
//
//import com.example.BC_alternance.dto.BorneDto;
//import com.example.BC_alternance.dto.UtilisateurDto;
//import com.example.BC_alternance.mapper.BorneMapper;
//import com.example.BC_alternance.mapper.BorneMapperImpl;
//import com.example.BC_alternance.model.Borne;
//import com.example.BC_alternance.model.Lieux;
//import com.example.BC_alternance.model.Reservation;
//import com.example.BC_alternance.model.Utilisateur;
//import com.example.BC_alternance.model.enums.RolesEnum;
//import com.example.BC_alternance.model.enums.StatusEnum;
//import com.example.BC_alternance.repository.BorneRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.Month;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//@ExtendWith(MockitoExtension.class)
//class BorneServiceImplTest {
//
//    @Mock
//    private BorneRepository borneRepository;
//
//    @Mock
//    private BorneMapper borneMapper;
//
//    @InjectMocks
//    private BorneServiceImpl borneServiceImpl;
//    @InjectMocks
//    private LieuxServiceImpl lieuxService;
//
//    @Test
//    public void testGetAllBornes() {
//
//        Lieux lieux = new Lieux();
//        lieux.setId(1L);
//        lieux.setVille("TESTVILLE");
//        lieux.setAdresse("TESTADRESSE");
//        lieux.setCodePostal("63000");
//
//        // Simuler une liste d'entités "Borne"
//        List<Borne> borneEntities = Arrays.asList(
//                new Borne(1L, "BorneTest1", 2F, true, "Instruction1", false, 1.43, 1.22, 3F, null, null, null, lieux),
//                new Borne(2L, "BorneTest2", 1F, false, "Instruction2", true, 0.43, 0.22, 2.4F, null, null, null, lieux)
//        );
//
//        // Simuler les DTO correspondants
//        List<BorneDto> allBornes = Arrays.asList(
//                new BorneDto(1L, "BorneTest1", 2F, true, "Instruction1", false, 1.43, 1.22, 3F, 1L, 1L, null, null),
//                new BorneDto(2L, "BorneTest2", 1F, false, "Instruction2", true, 0.43, 0.22, 2.4F, 2L, 1L, null, null)
//        );
//
//        // Configurer les mocks
//        when(borneRepository.findAll()).thenReturn(borneEntities);
//        when(borneMapper.toDto(any(Borne.class)))
//                .thenAnswer(invocation -> {
//                    Borne borne = invocation.getArgument(0);
//                    return allBornes.stream()
//                            .filter(dto -> dto.getId().equals(borne.getId()))
//                            .findFirst()
//                            .orElse(null);
//                });
//
//        // Appeler le service
//        List<BorneDto> bornes = borneServiceImpl.getAllBornes();
//
//        // Vérifications
//        verify(borneRepository, times(1)).findAll();
//        assertNotNull(bornes);
//        assertEquals(allBornes, bornes);
//    }
//
//
//    @Test
//    void testGetBorneById() {
//        Long borneId = 1L;
//        Lieux lieux = new Lieux();
//        lieux.setId(1L);
//        lieux.setVille("TESTVILLE");
//        lieux.setAdresse("TestAdresse");
//        lieux.setCodePostal("63000");
//
//        // Simuler une entité "Borne"
//        Borne borne = new Borne(
//                borneId, "BorneTest1", 2F, true, "Instruction1", false, 1.43, 1.22, 3F, null, null, null, lieux);
//
//        // Simuler le DTO correspondant
//        BorneDto borneDto = new BorneDto(
//                borneId, "BorneTest1", 2F, true, "Instruction1", false, 1.43, 1.22, 3F, 1L, 1L, null, null);
//
//        // Configurer les mocks
//        when(borneRepository.findById(borneId)).thenReturn(Optional.of(borne));
//        when(borneMapper.toDto(any(Borne.class))).thenReturn(borneDto);
//
//        // Appeler la méthode testée
//        BorneDto bornedto = borneServiceImpl.getBorneById(borneId);
//
//        // Vérifications
//        verify(borneRepository, times(1)).findById(borneId);
//        assertNotNull(bornedto);
//        assertEquals(borneDto, bornedto);
//    }
//
//
//    @Test
//    void testSaveBorne() {
//        // Créer les entités nécessaires pour le test
//        Lieux lieux = new Lieux();
//        lieux.setId(1L);
//        lieux.setVille("TESTVILLE");
//        lieux.setAdresse("TestAdresse");
//        lieux.setCodePostal("63000");
//
//        Utilisateur utilisateur = new Utilisateur();
//        utilisateur.setId(1L);
//        utilisateur.setNom("PEDRO");
//        utilisateur.setPrenom("Aurélie");
//        utilisateur.setEmail("aurelie@test.fr");
//        utilisateur.setMotDePasse("toto");
//        utilisateur.setNomRue("15 rue du test");
//        utilisateur.setCodePostal("63360");
//        utilisateur.setVille("GERZAT");
//        utilisateur.setTelephone("0701020304");
//        utilisateur.setRole(RolesEnum.PROPRIETAIRE);
//
//        // DTO à sauvegarder
//        BorneDto borneToSave = new BorneDto();
//        borneToSave.setNom("Borne 1");
//        borneToSave.setInstruction("Tournez à droite après le croisement de la boulangerie et la borne se trouve à 30m.");
//        borneToSave.setEstDisponible(true);
//        borneToSave.setLatitude(45.76567840576172);
//        borneToSave.setLongitude(3.125333309173584);
//        borneToSave.setLieuId(1L);
//        borneToSave.setPuissance(7.4F);
//        borneToSave.setPrix(2F);
//        borneToSave.setSurPied(true);
//        borneToSave.setUtilisateurId(1L);
//
//        // Entité simulée (après sauvegarde)
//        Borne savedBorne = new Borne();
//        savedBorne.setId(1L);
//        savedBorne.setNom("Borne 1");
//        savedBorne.setInstruction("Tournez à droite après le croisement de la boulangerie et la borne se trouve à 30m.");
//        savedBorne.setEstDisponible(true);
//        savedBorne.setLatitude(45.76567840576172);
//        savedBorne.setLongitude(3.125333309173584);
//        savedBorne.setLieux(lieux);
//        savedBorne.setPuissance(7.4F);
//        savedBorne.setPrix(2F);
//        savedBorne.setSurPied(true);
//        savedBorne.setUtilisateur(utilisateur);
//
//        // Configurer les mocks
//        when(borneMapper.toEntity(any(BorneDto.class))).thenReturn(savedBorne);
//        when(borneRepository.save(any(Borne.class))).thenReturn(savedBorne);
//        when(borneMapper.toDto(any(Borne.class))).thenReturn(borneToSave);
//
//        // Appeler la méthode testée
//        Borne result = borneServiceImpl.saveBorne(borneToSave);
//
//        // Vérifications
//        verify(borneMapper, times(1)).toEntity(borneToSave);
//        verify(borneRepository, times(1)).save(savedBorne);
//        verify(borneMapper, times(1)).toDto(savedBorne);
//
//        assertNotNull(result);
//        assertEquals(borneToSave.getNom(), result.getNom());
//        assertEquals(borneToSave.getInstruction(), result.getInstruction());
//        assertEquals(borneToSave.getLatitude(), result.getLatitude());
//        assertEquals(borneToSave.getLongitude(), result.getLongitude());
//    }
//
////    @Test
////    void testDeleteBorne() {
////        borneServiceImpl.deleteBorne(Long.valueOf(1));
////        verify(borneRepository).deleteById(any(Long.class));
////    }
//}
//
////Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme