package com.example.BC_alternance.config;

import com.example.BC_alternance.dto.*;
import com.example.BC_alternance.model.enums.RolesEnum;
import com.example.BC_alternance.model.enums.StatusEnum;
import com.example.BC_alternance.repository.UtilisateurRepository;
import com.example.BC_alternance.service.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

//@Component
@AllArgsConstructor
public class AjoutDonneesInitiales implements CommandLineRunner {
    @Autowired
    BorneServiceImpl borneService;
    @Autowired
    LieuxServiceImpl lieuxService;
    @Autowired
    ReservationServiceImpl reservationService;
    @Autowired
    UtilisateurServiceImpl utilisateurService;
    @Autowired
    MediaServiceImpl mediaService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private Environment env; // pour savoir quel profil est actif

    private boolean isProd() {
        return Arrays.asList(env.getActiveProfiles()).contains("prod");
    }

    private static final String url ="https://res.cloudinary.com/doq9rxixm/image/upload/";
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Profil actif : " + Arrays.toString(env.getActiveProfiles()));

            UtilisateurDto u1 = new UtilisateurDto();
            u1.setNom("PEDRO");
            u1.setPrenom("Aurélie");
            u1.setEmail("aurelie@test.fr");
            u1.setMotDePasse("Password1!");
            u1.setDateDeNaissance(LocalDate.of(1992, 05, 28));
            u1.setNomRue("15 rue du 11 novembre");
            u1.setCodePostal("63360");
            u1.setVille("GERZAT");
            u1.setTelephone("0701020304");
            u1.setEmailValidated(true);
            u1.setRole(RolesEnum.PROPRIO_LOCATAIRE);
        if (!utilisateurRepository.existsByEmail("aurelie@test.fr")) {
            utilisateurService.saveUtilisateur(u1);
            System.out.println("Utilisateur ajouté : aurelie@test.fr");
        }

            UtilisateurDto u2 = new UtilisateurDto();
            u2.setNom("CHANEL");
            u2.setPrenom("Coco");
            u2.setEmail("coco@test.fr");
            u2.setMotDePasse("Password1!");
            u2.setDateDeNaissance(LocalDate.of(1971, 01, 10));
            u2.setNomRue("55 avenue Jean Noellet");
            u2.setCodePostal("63170");
            u2.setVille("Aubière");
            u2.setTelephone("0605020334");
            u2.setEmailValidated(true);
            u2.setRole(RolesEnum.PROPRIO_LOCATAIRE);
        if (!utilisateurRepository.existsByEmail("coco@test.fr")) {
            utilisateurService.saveUtilisateur(u2);
            System.out.println("Utilisateur ajouté : coco@test.fr");
        }

            UtilisateurDto u3 = new UtilisateurDto();
            u3.setNom("MESSI");
            u3.setPrenom("Lionel");
            u3.setEmail("lionel@test.fr");
            u3.setMotDePasse("Password1!");
            u3.setDateDeNaissance(LocalDate.of(1988, 06, 29));
            u3.setNomRue("222 boulevard gustave flaubert");
            u3.setCodePostal("63000");
            u3.setVille("Clermont-Fd");
            u3.setTelephone("0705020334");
            u3.setEmailValidated(true);
            u3.setRole(RolesEnum.PROPRIO_LOCATAIRE);
        if (!utilisateurRepository.existsByEmail("lionel@test.fr")) {
            utilisateurService.saveUtilisateur(u3);
            System.out.println("Utilisateur ajouté : lionel@test.fr");
        }

            System.out.println(utilisateurService.getAllUtilisateurs());

            LieuxDto l1 = new LieuxDto();
            l1.setAdresse("Rue Simon Fryd");
            l1.setVille("Lyon");
            l1.setCodePostal("69007");
            lieuxService.saveLieux(l1);


            LieuxDto l2 = new LieuxDto();
            l2.setAdresse("130 Boulevard de la République");
            l2.setVille("Mauguio");
            l2.setCodePostal("34130");
            lieuxService.saveLieux(l2);

            LieuxDto l3 = new LieuxDto();
            l3.setAdresse("56 Rue de Romagnat");
            l3.setVille("Aubière");
            l3.setCodePostal("63170");
            lieuxService.saveLieux(l3);

            LieuxDto l4 = new LieuxDto();
            l4.setAdresse("Place du Docteur Peyneau");
            l4.setVille("Arcachon");
            l4.setCodePostal("33120");
            lieuxService.saveLieux(l4);

            LieuxDto l5 = new LieuxDto();
            l5.setAdresse("675 Av. du Palais de la Mer");
            l5.setVille("Le Grau-du-Roi");
            l5.setCodePostal("30240");
            lieuxService.saveLieux(l5);

            LieuxDto l6 = new LieuxDto();
            l6.setAdresse("5 Rue de l'Hôtel de ville");
            l6.setVille("Brive-la-Gaillarde");
            l6.setCodePostal("19100");
            lieuxService.saveLieux(l6);

            System.out.println(lieuxService.getAllLieux());


            BorneDto b1 = new BorneDto();
            b1.setNom("Borne 1");
            if (isProd()) {
                // En prod
                b1.setPhoto("https://res.cloudinary.com/doq9rxixm/image/upload/borneClermont1.jpg");
            } else {
                // En local
                b1.setPhoto("borneClermont1.jpg");
            }
            b1.setInstruction("Tournez à droite après le croisement de la boulangerie et a borne se trouve à 30m.");
            b1.setEstDisponible(true);
            b1.setLieuId(1L);
            b1.setPuissance(7.4F);
            b1.setPrix(2F);
            b1.setSurPied(true);
            b1.setUtilisateurId(1L);
            borneService.saveBorne(b1);

            BorneDto b2 = new BorneDto();
            b2.setNom("Borne 2");
            if (isProd()) {
                b2.setPhoto(url +"borne-electrique-plage.jpg");
            } else {
                b2.setPhoto("borne-electrique-plage.jpg");
            }
            b2.setInstruction("Juste à côté du carrouselle face à la plage");
            b2.setEstDisponible(true);
            b2.setLieuId(4L);
            b2.setPuissance(3.7F);
            b2.setPrix(3F);
            b2.setSurPied(true);
            b2.setUtilisateurId(2L);
            borneService.saveBorne(b2);

            BorneDto b3 = new BorneDto();
            b3.setNom("Borne 3");
            if (isProd()) {
                b3.setPhoto(url + "voitureBorneMur.jpeg");
            } else {
                b3.setPhoto("voitureBorneMur.jpeg");
            }
            b3.setInstruction("Instruction pour borne 3");
            b3.setEstDisponible(true);
            b3.setLieuId(3L);
            b3.setPuissance(2.3F);
            b3.setPrix(4F);
            b3.setSurPied(false);
            b3.setUtilisateurId(1L);
            borneService.saveBorne(b3);

            BorneDto b4 = new BorneDto();
            b4.setNom("Borne 4");
            if (isProd()) {
                b4.setPhoto(url + "voitureBorneDroitCote.jpg");
            } else {
                b4.setPhoto("voitureBorneDroitCote.jpg");
            }
            b4.setInstruction("Stationnez sur la place signalée par le panneau vert ‘Recharge’ devant la villa n°12");
            b4.setEstDisponible(true);
            b4.setLieuId(2L);
            b4.setPuissance(11F);
            b4.setPrix(5.5F);
            b4.setSurPied(true);
            b4.setUtilisateurId(2L);
            borneService.saveBorne(b4);

            BorneDto b5 = new BorneDto();
            b5.setNom("Borne 5");
            if (isProd()) {
                b5.setPhoto(url + "VoitureBorneDerriere.jpg");
            } else {
                b5.setPhoto("VoitureBorneDerriere.jpg");
            }
            b5.setInstruction("Place-toi à gauche de l’allée pavée");
            b5.setEstDisponible(true);
            b5.setLieuId(5L);
            b5.setPuissance(11F);
            b5.setPrix(3F);
            b5.setSurPied(false);
            b5.setUtilisateurId(2L);
            borneService.saveBorne(b5);

            BorneDto b6 = new BorneDto();
            b6.setNom("Borne 6");
            if (isProd()) {
                b6.setPhoto(url + "borneGarageDouble.jpg");
            } else {
                b6.setPhoto("borneGarageDouble.jpg");
            }
            b6.setInstruction("Garage accessible par code (fourni après réservation)");
            b6.setEstDisponible(true);
            b6.setLieuId(6L);
            b6.setPuissance(11F);
            b6.setPrix(3F);
            b6.setSurPied(false);
            b6.setUtilisateurId(3L);
            borneService.saveBorne(b6);

            System.out.println(borneService.getAllBornes());

            ReservationDto r1 = new ReservationDto();
            r1.setUtilisateurId(2L);
            r1.setBorneId(1L);
            r1.setDateDebut(LocalDateTime.of(2025, 07, 01, 14, 0, 0));
            r1.setDateFin(LocalDateTime.of(2025, 07, 11, 18, 0, 0));
            r1.setStatus(StatusEnum.ACCEPTER);
            reservationService.saveReservation(r1);

            ReservationDto r2 = new ReservationDto();
            r2.setUtilisateurId(2L);
            r2.setBorneId(3L);
            r2.setDateDebut(LocalDateTime.of(2025, 07, 28, 20, 0, 0));
            r2.setDateFin(LocalDateTime.of(2025, 07, 29, 7, 0, 0));
            r2.setStatus(StatusEnum.EN_ATTENTE);
            reservationService.saveReservation(r2);

            ReservationDto r3 = new ReservationDto();
            r3.setUtilisateurId(3L);
            r3.setBorneId(2L);
            r3.setDateDebut(LocalDateTime.of(2025, 03, 25, 9, 30, 0));
            r3.setDateFin(LocalDateTime.of(2025, 03, 26, 17, 0, 0));
            r3.setStatus(StatusEnum.ACCEPTER);
            reservationService.saveReservation(r3);

            System.out.println(reservationService.getAllReservations());


    }
}
