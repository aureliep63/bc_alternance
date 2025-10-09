package com.example.BC_alternance.dto;

import com.example.BC_alternance.model.enums.StatusEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    Long id;
    @NotNull(message = "Le choix de la date de début est obligatoire.")
    private LocalDateTime dateDebut;
    @NotNull(message = "Le choix de la date de fin est obligatoire.")
    private  LocalDateTime dateFin;
    private StatusEnum status;

    @Positive(message = "L'id de l'utilisateur doit avoir une valeur positive")
    private Long utilisateurId;
    @Positive(message = "L'id de la borne doit avoir une valeur positive")
    private Long borneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getBorneId() {
        return borneId;
    }

    public void setBorneId(Long borneId) {
        this.borneId = borneId;
    }
}
