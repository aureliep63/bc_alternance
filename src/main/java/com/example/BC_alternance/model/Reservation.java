package com.example.BC_alternance.model;

import com.example.BC_alternance.model.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le choix de la date de début est obligatoire.")
    private LocalDate dateDebut;
    @NotBlank(message = "Le choix de la date de fin est obligatoire.")
    private  LocalDate dateFin;
    @NotBlank(message = "Le choix de l'heure de début est obligatoire.")
    private LocalTime heureDebut;
    @NotBlank(message = "Le choix de l'heure de fin est obligatoire.")
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToOne
    @JsonIgnoreProperties({"bornes", "reservations"})
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JsonIgnoreProperties({"medias"})
    @JoinColumn(name = "borne_id")
    private Borne borne;
}
