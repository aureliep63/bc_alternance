package com.example.BC_alternance.dto;

import com.example.BC_alternance.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

     Long id;

    private LocalDate dateDebut;
    private  LocalDate dateFin;
    private  LocalTime heureDebut;
    private  LocalTime heureFin;
    private StatusEnum status;

    private Long utilisateurId;
    private Long borneId;

}
