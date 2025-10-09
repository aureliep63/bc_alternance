package com.example.BC_alternance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
public class BorneDto  {
     Long id;

     @NotBlank(message = "Le nom de la borne est obligatoire.")
     private String nom;
     private String photo;
     @NotNull(message = "La puissance de la borne est obligatoire.")
     private float puissance;
     private boolean estDisponible;
     private String instruction;
     private boolean surPied;
     @NotNull(message = "Le prix de la borne est obligatoire.")
     @Min(value = 0, message = "Le prix doit être supérieur à 0€")
     private float prix;

     private Long utilisateurId;
     private Long lieuId;
     private List<Long> mediasId;
     private List<Long> reservationsId;
     private LieuxDto lieux;

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

     public String getPhoto() {
          return photo;
     }

     public void setPhoto(String photo) {
          this.photo = photo;
     }

     public float getPuissance() {
          return puissance;
     }

     public void setPuissance(float puissance) {
          this.puissance = puissance;
     }

     public boolean isEstDisponible() {
          return estDisponible;
     }

     public void setEstDisponible(boolean estDisponible) {
          this.estDisponible = estDisponible;
     }

     public String getInstruction() {
          return instruction;
     }

     public void setInstruction(String instruction) {
          this.instruction = instruction;
     }

     public boolean isSurPied() {
          return surPied;
     }

     public void setSurPied(boolean surPied) {
          this.surPied = surPied;
     }


     public float getPrix() {
          return prix;
     }

     public void setPrix(float prix) {
          this.prix = prix;
     }

     public Long getUtilisateurId() {
          return utilisateurId;
     }

     public void setUtilisateurId(Long utilisateurId) {
          this.utilisateurId = utilisateurId;
     }

     public Long getLieuId() {
          return lieuId;
     }

     public void setLieuId(Long lieuId) {
          this.lieuId = lieuId;
     }

     public List<Long> getMediasId() {
          return mediasId;
     }

     public void setMediasId(List<Long> mediasId) {
          this.mediasId = mediasId;
     }

     public List<Long> getReservationsId() {
          return reservationsId;
     }

     public void setReservationsId(List<Long> reservationsId) {
          this.reservationsId = reservationsId;
     }

     public LieuxDto getLieux() {
          return lieux;
     }

     public void setLieux(LieuxDto lieux) {
          this.lieux = lieux;
     }


}
