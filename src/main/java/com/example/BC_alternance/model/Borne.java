package com.example.BC_alternance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;



@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Borne {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Le nom de la borne est obligatoire.")
    private String nom;
    @NotNull(message = "La puissance de la borne est obligatoire.")
    private float puissance;
    private String photo;
    private boolean estDisponible;
    private String instruction;
    private boolean surPied;
    @NotNull(message = "Le prix de la borne est obligatoire.")
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
   // @NotNull(message = "Le lieux de la borne est obligatoire.")
    private Lieux lieux;


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String nom;
//    private float puissance;
//    private String photo;
//    private boolean estDisponible;
//    private String instruction;
//    private boolean surPied;
//    private float prix;
//    @OneToMany(mappedBy ="borne", fetch = FetchType.EAGER)
//    private List<Media> medias = new ArrayList<>();
//    @OneToMany(mappedBy ="borne", fetch = FetchType.EAGER)
//    private List<Reservation> reservations = new ArrayList<>();
//    @ManyToOne
//    @JoinColumn(name = "utilisateur_id")
//    private Utilisateur utilisateur;
//    @ManyToOne
//    private Lieux lieux;








    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Le nom de la borne est obligatoire.") String getNom() {
        return nom;
    }

    public void setNom(@NotBlank(message = "Le nom de la borne est obligatoire.") String nom) {
        this.nom = nom;
    }

    @NotNull(message = "La puissance de la borne est obligatoire.")
    public float getPuissance() {
        return puissance;
    }

    public void setPuissance(@NotNull(message = "La puissance de la borne est obligatoire.") float puissance) {
        this.puissance = puissance;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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



    @NotNull(message = "Le prix de la borne est obligatoire.")
    @Min(value = 0, message = "Le prix doit être supérieur à 0€")
    public float getPrix() {
        return prix;
    }

    public void setPrix(@NotNull(message = "Le prix de la borne est obligatoire.") @Min(value = 0, message = "Le prix doit être supérieur à 0€") float prix) {
        this.prix = prix;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public @NotNull(message = "Le lieux de la borne est obligatoire.") Lieux getLieux() {
        return lieux;
    }

    public void setLieux(@NotNull(message = "Le lieux de la borne est obligatoire.") Lieux lieux) {
        this.lieux = lieux;
    }

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
                ", prix=" + prix +
                ", medias=" + medias +
                ", reservations=" + reservations +
                ", utilisateur=" + utilisateur +
                ", lieux=" + lieux +
                '}';
    }


}
