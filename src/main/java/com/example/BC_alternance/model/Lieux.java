package com.example.BC_alternance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lieux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'adresse du lieux est obligatoire.")
    private String adresse;
    @NotBlank(message = "Le code postal est obligatoire.")
    @Pattern(regexp = "\\d{5}", message = "Le code postal doit être composé de 5 chiffres.")
    private String codePostal;
    @NotBlank(message = "La ville est obligatoire.")
    private String ville;

    @OneToMany(mappedBy="lieux", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"lieux"})
    private List<Borne> bornes = new ArrayList<>();

    @Override
    public String toString() {
        return "Lieux{" +
                "id=" + id +
                ", adresse='" + adresse + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
              //  ", bornes=" + bornes +
                '}';
    }
}
