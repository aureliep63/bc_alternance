package com.example.BC_alternance.dto;

import com.example.BC_alternance.model.enums.RolesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDto {

     Long id;

     private String nom;
     private String prenom;
     private String email;
     private String motDePasse;
     private String telephone;
     private RolesEnum role;
     private LocalDate dateDeNaissance;
     private String nomRue;
     private String codePostal;
     private String ville;

     private List<Long> bornesId;
     private List<Long> reservationsId;
}
