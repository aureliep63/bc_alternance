package com.example.BC_alternance.model;

import com.example.BC_alternance.model.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le choix de la date de début est obligatoire.")
    private LocalDateTime dateDebut;
    @NotNull(message = "Le choix de la date de fin est obligatoire.")
    private  LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToOne
    @JsonIgnoreProperties({"reservations"})
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JsonIgnoreProperties({"medias"})
    @JoinColumn(name = "borne_id")
    private Borne borne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "Le choix de la date de début est obligatoire.") LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(@NotNull(message = "Le choix de la date de début est obligatoire.") LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public @NotNull(message = "Le choix de la date de fin est obligatoire.") LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(@NotNull(message = "Le choix de la date de fin est obligatoire.") LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Borne getBorne() {
        return borne;
    }

    public void setBorne(Borne borne) {
        this.borne = borne;
    }


}
