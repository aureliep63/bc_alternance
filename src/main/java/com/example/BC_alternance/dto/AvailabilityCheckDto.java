package com.example.BC_alternance.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityCheckDto {

    private Long borneId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    public Long getBorneId() {
        return borneId;
    }

    public void setBorneId(Long borneId) {
        this.borneId = borneId;
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
}
