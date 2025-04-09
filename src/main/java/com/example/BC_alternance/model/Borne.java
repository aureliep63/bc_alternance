package com.example.BC_alternance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la borne est obligatoire.")
    private String nom;

    @NotBlank(message = "La puissance de la borne est obligatoire.")
    private float puissance;

    private String photo;

    private boolean estDisponible;

    private String instruction;

    private boolean surPied;

    @NotBlank(message = "La latitude de la borne est obligatoire.")
    private double latitude;
    @NotBlank(message = "La longitude de la borne est obligatoire.")
    private double longitude;
    @NotBlank(message = "Le prix de la borne est obligatoire.")
    @Min(value = 0, message = "Le prix doit être supérieur à 0€")
    private float prix;

    @OneToMany(mappedBy ="borne", fetch = FetchType.EAGER)
    private List<Media> medias = new ArrayList<>();

    @OneToMany(mappedBy ="borne", fetch = FetchType.EAGER)
    private List<Reservation> reservations = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JsonIgnoreProperties({"bornes"})
    @NotBlank(message = "Le lieux de la borne est obligatoire.")
    private Lieux lieux;


    @Override
    public String toString() {
        return "Borne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", puissance=" + puissance +
                ", photo='" + photo + '\'' +
                ", estDisponible=" + estDisponible +
                ", instruction='" + instruction + '\'' +
                ", surPied=" + surPied +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", prix=" + prix +
                ", medias=" + medias +
                ", reservations=" + reservations +
                ", utilisateur=" + utilisateur +
                ", lieux=" + lieux +
                '}';
    }
}
