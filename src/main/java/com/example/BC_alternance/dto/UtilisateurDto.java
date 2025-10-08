package com.example.BC_alternance.dto;

import com.example.BC_alternance.model.enums.RolesEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDto {

     Long id;
     @NotBlank(message = "Votre nom est obligatoire.")
     private String nom;
     @NotBlank(message = "Votre prénom est obligatoire.")
     private String prenom;
     @NotBlank(message = "Votre adresse mail est obligatoire.")
     @Email(message = "L'adresse e-mail n'est pas valide.")
     private String email;
     @NotBlank(message = "Le mot de passe est obligatoire.")
     @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "Le mot de passe doit contenir au minimum 8 caractères, dont une majuscule, une minuscule, un chiffre et un caractère spécial."
     )
     private String motDePasse;
     @NotBlank(message = "Votre numéro de téléphone est obligatoire.")
     @Pattern(regexp = "^(0[1-9])([ .-]?([0-9]{2})){4}$", message = "Le numéro de téléphone doit être au format valide (ex: 0612345678).")
     private String telephone;
     private RolesEnum role;
     private LocalDate dateDeNaissance;
     @NotBlank(message = "Votre adresse postale est obligatoire.")
     private String nomRue;
     @NotBlank(message = "Votre code postal est obligatoire.")
     @Pattern(regexp = "\\d{5}", message = "Le code postal doit être composé de 5 chiffres.")
     private String codePostal;
     @NotBlank(message = "Votre ville est obligatoire.")
     private String ville;

     private List<Long> bornesId;
     private List<Long> reservationsId;

     private String validationCode;
     private boolean emailValidated;

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getNom() {
          return nom;
     }

     public void setNom(String nom) {
          this.nom = nom;
     }

     public String getPrenom() {
          return prenom;
     }

     public void setPrenom(String prenom) {
          this.prenom = prenom;
     }

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public String getMotDePasse() {
          return motDePasse;
     }

     public void setMotDePasse(String motDePasse) {
          this.motDePasse = motDePasse;
     }

     public String getTelephone() {
          return telephone;
     }

     public void setTelephone(String telephone) {
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

     public String getNomRue() {
          return nomRue;
     }

     public void setNomRue(String nomRue) {
          this.nomRue = nomRue;
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

     public List<Long> getReservationsId() {
          return reservationsId;
     }

     public void setReservationsId(List<Long> reservationsId) {
          this.reservationsId = reservationsId;
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
}
