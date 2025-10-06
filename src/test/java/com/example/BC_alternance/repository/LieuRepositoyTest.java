package com.example.BC_alternance.repository;

import com.example.BC_alternance.model.Lieux;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest //charge context minim
class LieuxRepositoryTest {

    @Autowired // injection du vrai bean lieuRepo
    private LieuxRepository lieuxRepository;

    // log pour message pendant le test
    private static final Logger log = LoggerFactory.getLogger(
            LieuxRepositoryTest.class);

    @BeforeEach // méth exécuter avec le test
    void init(TestInfo testInfo) {
        log.info("Début du test : {}", testInfo.getDisplayName());
    }


    @Test
    void testSaveValidLieux() { // vérif sauvegarde d'un lieu valide
        // création de l'entité valide
        Lieux lieux = new Lieux();
        lieux.setAdresse("222 boulevard Gustave Flaubert");
        lieux.setVille("Clermont-Ferrand");
        lieux.setCodePostal("63000");
        Lieux saved = lieuxRepository.save(lieux); // appel du vrai repo pour sauvegarder
        assertThat(saved.getId()).isNotNull(); // verif d'un id généré
        assertThat(saved.getVille()).isEqualTo("Clermont-Ferrand"); // vérif que cette ville = ville définie
    }
    @Test
    void testSaveInvalidLieux() { // vérif sauvegarde d'un lieu invalide
        // création de l'entité invalide
        Lieux lieux = new Lieux();
        lieux.setAdresse("");  // NotBlank donc KO
        lieux.setVille(null);  // NotBlank donc KO
        lieux.setCodePostal("123");     // Pattern doit être 5 chiffres donc KO
      // force sauvegarde donc validation direct ici avec exept
        assertThatThrownBy(() -> {
            try {
                lieuxRepository.saveAndFlush(lieux);
            } catch (ConstraintViolationException e) {
                // Affiche chaque violation dans la console
                e.getConstraintViolations().forEach(v ->
                        System.out.println(v.getPropertyPath() + " : " + v.getMessage())
                );
                throw e;
            }
        })
                .isInstanceOf(ConstraintViolationException.class) // reception d'une contrainte de validation non respecté
                //vérif que msg d'erreur contient les messages attendus
                .hasMessageContaining("L'adresse du lieux est obligatoire")
                .hasMessageContaining("La ville est obligatoire")
                .hasMessageContaining("Le code postal doit être composé de 5 chiffres");
    }
}
