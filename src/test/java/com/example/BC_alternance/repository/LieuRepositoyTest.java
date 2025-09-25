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

@DataJpaTest
class LieuxRepositoryTest {

    @Autowired
    private LieuxRepository lieuxRepository;

    private static final Logger log = LoggerFactory.getLogger(
            LieuxRepositoryTest.class);

    @BeforeEach
    void init(TestInfo testInfo) {
        log.info("Début du test : {}", testInfo.getDisplayName());
    }


    @Test
    void testSaveValidLieux() {
        Lieux lieux = new Lieux();
        lieux.setAdresse("222 boulevard Gustave Flaubert");
        lieux.setVille("Clermont-Ferrand");
        lieux.setCodePostal("63000");
        Lieux saved = lieuxRepository.save(lieux);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVille()).isEqualTo("Clermont-Ferrand");
    }
    @Test
    void testSaveInvalidLieux() {
        Lieux lieux = new Lieux();
        lieux.setAdresse("");           // NotBlank donc KO
        lieux.setVille(null);           // NotBlank donc KO
        lieux.setCodePostal("123");     // Pattern doit être 5 chiffres donc KO
        assertThatThrownBy(() -> lieuxRepository.saveAndFlush(lieux))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("L'adresse du lieux est obligatoire")
                .hasMessageContaining("La ville est obligatoire")
                .hasMessageContaining("Le code postal doit être composé de 5 chiffres");
    }
}
