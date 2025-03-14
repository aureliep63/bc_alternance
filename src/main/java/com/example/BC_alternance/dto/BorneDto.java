package com.example.BC_alternance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorneDto  {
     Long id;

     private String nom;
     private float puissance;
     private boolean estDisponible;
     private String instruction;
     private boolean surPied;
     private double latitude;
     private double longitude;
     private float prix;

     private Long utilisateurId;
     private Long lieuId;
     private List<Long> mediasId;
     private List<Long> reservationsId;



}
