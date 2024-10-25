//package com.example.BC_alternance.repository;
//
//
//import com.example.BC_alternance.model.Borne;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;

//@DataJpaTest
//public class BorneRepositoryTest {
//
//    @Autowired
//    BorneRepository borneRepository;
//
//    @Test
//    void testSave() {
//        // Vérifie si la sauvegarde se fait correctement
//        //on donne un nom à la borne
//        String nom = "Borne test";
//
//        // donc on créer une borne "borne 1"
//        Borne borne1 = new Borne();
//        //on lui donne le nom borne test
//        borne1.setNom(nom);
//
//        // on créé la borne 2
//        Borne borne2 = new Borne();
//        //on lui donne le nom borne test
//        borne2.setNom(nom);
//        //et on la sauvegarde dans le repo
//        Borne borneEnregistree = borneRepository.save(borne2);
//
//        // On vérifie que le nom de la borne enregistrée (borne 2) est égale au nom utilisé pour instancier la borne test
//        assertEquals(nom, borneEnregistree.getNom());
//        // on vérifie que le nom de la borne enregistrée (borne 2) est égale au nom de la borne créé précédemment donc borne 1
//        assertThat(borneEnregistree.getNom()).isEqualTo(borne1.getNom());
//        // On vérifie que l'id de la borne enregistrée est positif'
//        assertThat(borneEnregistree.getId()).isPositive();
//    }
//
//    @Test
//    void testGet() {
//        // on test le findById du repo fonctionne
//        // on instancie un nom" borne test"
//        String nom = "Borne test";
//    // on créé la borne1
//        Borne borne1 = new Borne();
//        // on lui set le nom
//        borne1.setNom(nom);
//        // on la sauvegarde dans le repo
//        Borne borneSauvegarde = borneRepository.save(borne1);
//        // on créé 'borneRecup' qui est le résultat de findById du repo
//        Borne borneRecup = borneRepository.findById(borneSauvegarde.getId()).get(); // on trouve et le .get va le donner
//        // On vérifie que le nom de la borne récupéré est égale au nom utilisé pour instancier la borne 1
//        assertEquals(nom, borneRecup.getNom());
//        // on vérifie sur le nom de la borneRécup et égale au nom de la borneSauvegardé
//        assertThat(borneRecup.getNom()).isEqualTo(borneSauvegarde.getNom());
//        // On vérifie que l'id de la borne enregistrée est positif'
//        assertThat(borneRecup.getId()).isPositive();
//    }
//}