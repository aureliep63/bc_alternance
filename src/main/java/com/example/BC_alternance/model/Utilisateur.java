package com.example.BC_alternance.model;

import com.example.BC_alternance.model.enums.RolesEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
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
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire.")
    private String motDePasse;
    @NotBlank(message = "Votre numéro de téléphone est obligatoire.")
    @Pattern(regexp = "^(0[1-9])([ .-]?([0-9]{2})){4}$", message = "Le numéro de téléphone doit être au format valide (ex: 0612345678).")
    private String telephone;


    @Column(name = "role")
    private RolesEnum role;

    private LocalDate dateDeNaissance;
    @NotBlank(message = "Votre adresse postale est obligatoire.")
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

    private String validationCode;
    private boolean emailValidated = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Votre nom est obligatoire.") String getNom() {
        return nom;
    }

    public void setNom(@NotBlank(message = "Votre nom est obligatoire.") String nom) {
        this.nom = nom;
    }

    public @NotBlank(message = "Votre prénom est obligatoire.") String getPrenom() {
        return prenom;
    }

    public void setPrenom(@NotBlank(message = "Votre prénom est obligatoire.") String prenom) {
        this.prenom = prenom;
    }

    public @NotBlank(message = "Votre adresse mail est obligatoire.") @Email(message = "L'adresse e-mail n'est pas valide.") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Votre adresse mail est obligatoire.") @Email(message = "L'adresse e-mail n'est pas valide.") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Le mot de passe est obligatoire.") String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(@NotBlank(message = "Le mot de passe est obligatoire.") String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public @NotBlank(message = "Votre numéro de téléphone est obligatoire.") @Pattern(regexp = "^(0[1-9])([ .-]?([0-9]{2})){4}$", message = "Le numéro de téléphone doit être au format valide (ex: 06 12 34 56 78).") String getTelephone() {
        return telephone;
    }

    public void setTelephone(@NotBlank(message = "Votre numéro de téléphone est obligatoire.") @Pattern(regexp = "^(0[1-9])([ .-]?([0-9]{2})){4}$", message = "Le numéro de téléphone doit être au format valide (ex: 06 12 34 56 78).") String telephone) {
        this.telephone = telephone;
    }

    public RolesEnum getRole() {
        return role;
    }

    public void setRole(RolesEnum role) {
        this.role = role;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public @NotBlank(message = "Votre adresse psotale est obligatoire.") String getNomRue() {
        return nomRue;
    }

    public void setNomRue(@NotBlank(message = "Votre adresse psotale est obligatoire.") String nomRue) {
        this.nomRue = nomRue;
    }

    public @NotBlank(message = "Votre code postal est obligatoire.") @Pattern(regexp = "\\d{5}", message = "Le code postal doit être composé de 5 chiffres.") String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(@NotBlank(message = "Votre code postal est obligatoire.") @Pattern(regexp = "\\d{5}", message = "Le code postal doit être composé de 5 chiffres.") String codePostal) {
        this.codePostal = codePostal;
    }

    public @NotBlank(message = "Votre ville est obligatoire.") String getVille() {
        return ville;
    }

    public void setVille(@NotBlank(message = "Votre ville est obligatoire.") String ville) {
        this.ville = ville;
    }

    public List<Borne> getBornes() {
        return bornes;
    }

    public void setBornes(List<Borne> bornes) {
        this.bornes = bornes;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

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
