package com.example.BC_alternance.repository;

import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.model.enums.RolesEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BorneRepositoryTest {

    @Autowired
    private BorneRepository borneRepository;
    @Autowired
    private LieuxRepository lieuxRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    private static final Logger log = LoggerFactory.getLogger(
            BorneRepositoryTest.class);
    private Lieux lieu;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        log.info(" Début du test : {}", testInfo.getDisplayName());

        lieu = createLieu("Clermont-Ferrand",
                "222 boulevard Gustave Flaubert", "63000");
    }

    //  Méthodes utilitaires pour éviter la duplication

    private Lieux createLieu(String ville,
                             String adresse, String codePostal)
    {
        Lieux l = new Lieux();
        l.setVille(ville);
        l.setAdresse(adresse);
        l.setCodePostal(codePostal);
        return lieuxRepository.save(l);
    }

    private Borne createBorne(String nom, int prix, Lieux l) {
        Borne b = new Borne();
        b.setNom(nom);
        b.setPrix(prix);
        b.setLieux(l);
        return borneRepository.save(b);
    }

    private Utilisateur createUtilisateur(String prenom, String nom) {
        Utilisateur u = new Utilisateur();
        u.setPrenom(prenom);
        u.setNom(nom);
        u.setEmail(prenom.toLowerCase() + "@test.com");
        u.setTelephone("0101010101");
        u.setNomRue("222 boulevard Gustave Flaubert");
        u.setCodePostal("63000");
        u.setVille("Clermont-Ferrand");
        u.setRole(RolesEnum.PROPRIO_LOCATAIRE);
        u.setMotDePasse("Password1!");
        return utilisateurRepository.save(u);
    }

    private Reservation createReservation(
            Borne borne, LocalDateTime debut, LocalDateTime fin) {
        Reservation r = new Reservation();
        r.setBorne(borne);
        r.setDateDebut(debut);
        r.setDateFin(fin);
        return reservationRepository.save(r);
    }


    @Test
    void shouldSaveAndFindBorneById() {
        Borne saved = createBorne("BorneTest", 150, lieu);

        Optional<Borne> found = borneRepository.findById(saved.getId());

        assertThat(found).isPresent()
                .get()
                .satisfies(b -> assertThat(b.getNom()).isEqualTo("BorneTest"));
    }

    @Test
    void shouldFindAllBornes() {
        createBorne("B1", 100, lieu);
        createBorne("B2", 200, lieu);

        List<Borne> all = borneRepository.findAll();

        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldDeleteBorneById() {
        Borne saved = createBorne("ToDelete", 50, lieu);

        borneRepository.deleteById(saved.getId());

        assertThat(borneRepository.findById(saved.getId())).isNotPresent();
    }

    @Test
    void shouldFindBornesByUtilisateurId() {
        Utilisateur user = createUtilisateur("Prenom-Test", "NOMTEST");
        Borne borne = createBorne("BorneUser", 120, lieu);
        borne.setUtilisateur(user);
        borneRepository.save(borne);

        List<Borne> result = borneRepository.findByUtilisateurId(user.getId());

        assertThat(result).isNotEmpty()
                .extracting(Borne::getNom)
                .contains("BorneUser");
    }

    @Test
    void shouldFindBornesByReservationId() {
        Borne borne = createBorne("BorneResa", 130, lieu);
        Reservation resa = createReservation(borne, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        List<Borne> result = borneRepository.findByReservations_Id(resa.getId());

        assertThat(result).hasSize(1)
                .extracting(Borne::getNom)
                .contains("BorneResa");
    }

    @Test
    void shouldFindOnlyAvailableBornes() {
        Lieux lyon = createLieu("Lyon", "Place Bellecour", "69000");
        Borne borneLibre = createBorne("BorneLibre", 140, lyon);
        Borne borneOccupee = createBorne("BorneOccupee", 160, lyon);
        createReservation(borneOccupee, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(5));

        LocalDateTime debut = LocalDateTime.now().plusDays(3);
        LocalDateTime fin = LocalDateTime.now().plusDays(4);

        List<Borne> result = borneRepository.findBornesWithVilleAndDateRange("lyon%", debut, fin);

        assertThat(result).extracting(Borne::getNom)
                .contains("BorneLibre")
                .doesNotContain("BorneOccupee");
    }
}
