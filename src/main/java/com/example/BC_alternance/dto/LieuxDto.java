package com.example.BC_alternance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class LieuxDto  {

     Long id;
    @NotBlank(message = "L'adresse du lieux est obligatoire.")
   private   String adresse;
    @NotBlank(message = "Le code postal est obligatoire.")
    @Pattern(regexp = "\\d{5}", message = "Le code postal doit être composé de 5 chiffres.")
    private String codePostal;
    @NotBlank(message = "La ville est obligatoire.")
    private   String ville;
   private List<Long> bornesId;

    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public List<Long> getBornesId() {
        return bornesId;
    }

    public void setBornesId(List<Long> bornesId) {
        this.bornesId = bornesId;
    }
}
