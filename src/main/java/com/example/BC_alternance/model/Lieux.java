package com.example.BC_alternance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Lieux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "L'adresse du lieux est obligatoire.")
    private String adresse;
    @NotBlank(message = "Le code postal est obligatoire.")
    @Pattern(regexp = "\\d{5}",
            message = "Le code postal doit être composé de 5 chiffres.")
    private String codePostal;
    @NotBlank(message = "La ville est obligatoire.")
    @Column(name = "ville", columnDefinition = "TEXT")
    private String ville;

    @OneToMany(mappedBy="lieux", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"lieux"})
    private List<Borne> bornes = new ArrayList<>();
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "L'adresse du lieux est obligatoire.") String getAdresse() {
        return adresse;
    }

    public void setAdresse(@NotBlank(message = "L'adresse du lieux est obligatoire.") String adresse) {
        this.adresse = adresse;
    }

    public @NotBlank(message = "Le code postal est obligatoire.") @Pattern(regexp = "\\d{5}",
            message = "Le code postal doit être composé de 5 chiffres.") String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(@NotBlank(message = "Le code postal est obligatoire.") @Pattern(regexp = "\\d{5}",
            message = "Le code postal doit être composé de 5 chiffres.") String codePostal) {
        this.codePostal = codePostal;
    }

    public @NotBlank(message = "La ville est obligatoire.") String getVille() {
        return ville;
    }

    public void setVille(@NotBlank(message = "La ville est obligatoire.") String ville) {
        this.ville = ville;
    }

    public List<Borne> getBornes() {
        return bornes;
    }

    public void setBornes(List<Borne> bornes) {
        this.bornes = bornes;
    }

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
}




