package com.example.BC_alternance.model;

import com.example.BC_alternance.model.enums.RolesEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Votre nom est obligatoire.")
    private String nom;
    @NotBlank(message = "Votre prénom est obligatoire.")
    private String prenom;

    @NotBlank(message = "Votre adresse mail est obligatoire.")
    @Email(message = "L'adresse e-mail n'est pas valide.")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire.")
    private String motDePasse;
    @NotBlank(message = "Votre numéro de téléphone est obligatoire.")
    @Pattern(regexp = "^(0[1-9])([ .-]?([0-9]{2})){4}$", message = "Le numéro de téléphone doit être au format valide (ex: 06 12 34 56 78).")
    private String telephone;


    @Column(name = "role")
    private RolesEnum role;

    private LocalDate dateDeNaissance;
    @NotBlank(message = "Votre adresse psotale est obligatoire.")
    private String nomRue;
    @NotBlank(message = "Votre code postal est obligatoire.")
    @Pattern(regexp = "\\d{5}", message = "Le code postal doit être composé de 5 chiffres.")
    private String codePostal;
    @NotBlank(message = "Votre ville est obligatoire.")
    private String ville;

    @OneToMany(mappedBy ="utilisateur", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"utilisateurs"})
    private List<Borne> bornes = new ArrayList<>();

    @OneToMany(mappedBy ="utilisateur", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"utilisateurs"})
    private List<Reservation> reservations = new ArrayList<>();


    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", telephone='" + telephone + '\'' +
               // ", role=" + role +
                ", dateDeNaissance=" + dateDeNaissance +
                ", nomRue='" + nomRue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }
}
