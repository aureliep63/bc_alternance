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
public class LieuxDto  {

     Long id;

   private   String adresse;
    private String codePostal;
   private   String ville;
   private List<Long> bornesId;
}
