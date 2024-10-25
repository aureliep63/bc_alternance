package com.example.BC_alternance.initialisation;

import com.example.BC_alternance.dto.*;
import com.example.BC_alternance.model.*;
import com.example.BC_alternance.model.enums.MediaTypeEnum;
import com.example.BC_alternance.model.enums.RolesEnum;
import com.example.BC_alternance.model.enums.StatusEnum;
import com.example.BC_alternance.service.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@AllArgsConstructor
public class AjoutDonneesInitiales implements CommandLineRunner {

    BorneServiceImpl borneService;
    LieuxServiceImpl lieuxService;
    ReservationServiceImpl reservationService;
    UtilisateurServiceImpl utilisateurService;
    MediaServiceImpl mediaService;

    @Override
    public void run(String... args) throws Exception{

        UtilisateurDto u1 = new UtilisateurDto();
        u1.setNom("PEDRO");
        u1.setPrenom("Aurélie");
        u1.setEmail("aurelie@test.fr");
        u1.setMotDePasse("toto");
        u1.setDateDeNaissance(LocalDate.of(1992,05,28));
        u1.setNomRue("15 rue du test");
        u1.setCodePostal("63360");
        u1.setVille("GERZAT");
        u1.setTelephone("0701020304");
        u1.setRole(RolesEnum.PROPRIETAIRE);
        utilisateurService.saveUtilisateur(u1);

        UtilisateurDto u2 = new UtilisateurDto();
        u2.setNom("CHANEL");
        u2.setPrenom("Coco");
        u2.setEmail("coco@test.fr");
        u2.setMotDePasse("toto");
        u2.setDateDeNaissance(LocalDate.of(1971,01,10));
        u2.setNomRue("23 rue du saumur");
        u2.setCodePostal("63170");
        u2.setVille("Aubière");
        u2.setTelephone("0605020334");
        u2.setRole(RolesEnum.LOCATAIRE);
        utilisateurService.saveUtilisateur(u2);

        System.out.println(utilisateurService.getAllUtilisateurs());


        LieuxDto l1 = new LieuxDto();
        l1.setAdresse("175 boulevard Gustave Flaubert");
        l1.setVille("Clermont-Ferrand");
        l1.setCodePostal("63000");
        lieuxService.saveLieux(l1);


        LieuxDto l2= new LieuxDto();
        l2.setAdresse("place des Ramacles");
        l2.setVille("Aubière");
        l2.setCodePostal("63170");
        lieuxService.saveLieux(l2);

        LieuxDto l3 = new LieuxDto();
        l3.setAdresse("17 rue Georges Gouy");
        l3.setVille("Lyon");
        l3.setCodePostal("69007");
        lieuxService.saveLieux(l3);

        LieuxDto l4 = new LieuxDto();
        l4.setAdresse("156 boulevard de la plage");
        l4.setVille("Arcachon");
        l4.setCodePostal("33120");
        lieuxService.saveLieux(l4);

        System.out.println(lieuxService.getAllLieux());


        BorneDto b1 = new BorneDto();
        b1.setNom("Borne 1");
        b1.setInstruction("Tournez à droite après le croisement de la boulangerie et a borne se trouve à 30m.");
        b1.setEstDisponible(true);
        b1.setLatitude(45.76567840576172);
        b1.setLongitude(3.125333309173584);
        b1.setLieuId(1L);
        b1.setPuissance(7.4F);
        b1.setPrix(2F);
        b1.setSurPied(true);
        b1.setUtilisateurId(1L);
        borneService.saveBorne(b1);

        BorneDto b2 = new BorneDto();
        b2.setNom("Borne 2");
        b2.setInstruction("Juste à côté du carrouselle face à la plage");
        b2.setEstDisponible(true);
        b2.setLatitude(44.66270065307617);
        b2.setLongitude(-1.1636296510696411);
        b2.setLieuId(4L);
        b2.setPuissance(3.2F);
        b2.setPrix(3F);
        b2.setSurPied(true);
        b2.setUtilisateurId(2L);
        borneService.saveBorne(b2);

        System.out.println(borneService.getAllBornes());

        ReservationDto r1 = new ReservationDto();
        r1.setUtilisateurId(2L);
        r1.setBorneId(1L);
        r1.setDateDebut(LocalDate.of(2024,10,11));
        r1.setDateFin(LocalDate.of(2024,10,11));
        r1.setHeureDebut(LocalTime.of(14,0,0));
        r1.setHeureFin(LocalTime.of(18,0,0));
        r1.setStatus(StatusEnum.ACCEPTER);
        reservationService.saveReservation(r1);

        ReservationDto r2 = new ReservationDto();
        r2.setUtilisateurId(1L);
        r2.setBorneId(1L);
        r2.setDateDebut(LocalDate.of(2024,10,28));
        r2.setDateFin(LocalDate.of(2024,10,29));
        r2.setHeureDebut(LocalTime.of(20,30,0));
        r2.setHeureFin(LocalTime.of(7,0,0));
        r2.setStatus(StatusEnum.EN_ATTENTE);
        reservationService.saveReservation(r2);

        System.out.println(reservationService.getAllReservations());

        MediaDto m1 = new MediaDto();
        m1.setTypeMedia(MediaTypeEnum.PHOTO);
        m1.setLibelle("/upload/borneClermont1.jpg");
        m1.setBorneId(1L);
        mediaService.saveMedia(m1);

        MediaDto m2 = new MediaDto();
        m1.setTypeMedia(MediaTypeEnum.PHOTO);
        m2.setLibelle("/upload/borneClermont.jpg");
        m2.setBorneId(1L);
        mediaService.saveMedia(m2);

        MediaDto m3 = new MediaDto();
        m1.setTypeMedia(MediaTypeEnum.PHOTO);
        m3.setLibelle("/upload/borne-electrique-plage.jpg");
        m3.setBorneId(2L);
        mediaService.saveMedia(m3);

        System.out.println(mediaService.getAllMedias());

    }


}
