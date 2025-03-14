//package com.example.BC_alternance.repository;
//
//
//import com.example.BC_alternance.model.Borne;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.util.AssertionErrors.assertFalse;
//
//@DataJpaTest
//public class BorneRepositoryTest {
//
//    @Autowired
//    BorneRepository borneRepository;
//
//    @Test
//    @DirtiesContext
//    void testGetAll() {
//        // on test le findAll du repo fonctionne
//
//        // on créé la borne1
//        Borne borne1 = new Borne();
//        borne1.setNom("Borne1");
//        borneRepository.save(borne1);
//
//        Borne borne2 = new Borne();
//        borne2.setNom("Borne2");
//        borneRepository.save(borne2);
//
//        List<Borne> bornes = borneRepository.findAll();
//
//
//        // on vérifie sur le nom de la borne1 et égale "Borne1"
//        assertThat(borne1.getNom()).isEqualTo("Borne1");
//        // On vérifie que l'id de la borne enregistrée est positif'
//        assertThat(borne2.getId()).isPositive();
//        assertEquals(2, bornes.size()); // si je met 5 ca ne fonctionne pas
//    }
//
//    @Test
//    @DirtiesContext
//    void testGet() {
//        // on test le findById du repo fonctionne
//
//        // on créé la borne1 avec un nom et on sauvegarde
//        Borne borne1 = new Borne();
//        borne1.setNom("Borne test");
//        Borne borneSauvegarde = borneRepository.save(borne1);
//
//        // on créé 'borneRecup' qui est le résultat de findById du repo
//        Borne borneRecup = borneRepository.findById(borneSauvegarde.getId()).get(); // on trouve et le .get va le donner
//
//        // On vérifie que le nom de la borne récupéré est égale au nom utilisé pour instancier la borne 1
//        assertEquals("Borne test", borneRecup.getNom());
//        // on vérifie sur le nom de la borneRécup et égale au nom de la borneSauvegardé
//        assertThat(borneRecup.getNom()).isEqualTo(borneSauvegarde.getNom());
//        // On vérifie que l'id de la borne enregistrée est positif'
//        assertThat(borneRecup.getId()).isPositive();
//    }
//    @Test
//    @DirtiesContext
//    void testSave() {
//        // Vérifie si la sauvegarde se fait correctement
//
//
//        // on créé la borne 2
//        Borne borne2 = new Borne();
//        borne2.setId(2L);
//        borne2.setNom("Borne testRepo");
//        //et on la sauvegarde dans le repo
//        Borne borneEnregistree = borneRepository.save(borne2);
//
//        // On vérifie que le nom de la borne enregistrée (borne 2) est égale au nom utilisé pour instancier la borne test
//        assertEquals("Borne testRepo", borneEnregistree.getNom());
//        // on vérifie que le nom de la borne enregistrée (borne 2) est égale au nom de la borne créé précédemment donc borne 1
//        assertThat(borneEnregistree.getNom()).isEqualTo(borne2.getNom());
//        // On vérifie que l'id de la borne enregistrée est positif'
//        assertThat(borneEnregistree.getId()).isPositive();
//
//
//    }
//
//    @Test
//    @DirtiesContext
//    void testUpdate() {
//        // on veut test la mise à jour d'une borne
//        Borne borne1 = new Borne();
//        borne1.setId(1L);
//        borne1.setNom("Borne testRepo");
//        borneRepository.save(borne1);
//
//        Borne borne = borneRepository.findById(1L).get();
//        borne1.setNom("Borne Update");
//       Borne borneUpdate =borneRepository.save(borne1);
//
//        assertEquals("Borne Update", borneUpdate.getNom());
//    }
//
//    @Test
//    @DirtiesContext
//    void testDelete() {
//        // on test la suppression
//
//        Borne borne1 = new Borne();
//        borne1.setId(1L);
//        borneRepository.save(borne1);
//
//        Borne borneDelete = borneRepository.findById(1L).get();
//        borneRepository.delete(borneDelete);
//
//        assertFalse("Person not deleted", borneRepository.existsById(1L));
//
//    }
//}