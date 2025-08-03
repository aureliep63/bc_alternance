package com.example.BC_alternance.model;

import com.example.BC_alternance.model.enums.MediaTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le choix du fichier est obligatoire.")
    private String libelle;


    @Enumerated(EnumType.STRING)
    private MediaTypeEnum typeMedia;

    @ManyToOne
    @JsonIgnoreProperties({"medias"})
    private Borne borne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Le choix du fichier est obligatoire.") String getLibelle() {
        return libelle;
    }

    public void setLibelle(@NotBlank(message = "Le choix du fichier est obligatoire.") String libelle) {
        this.libelle = libelle;
    }

    public MediaTypeEnum getTypeMedia() {
        return typeMedia;
    }

    public void setTypeMedia(MediaTypeEnum typeMedia) {
        this.typeMedia = typeMedia;
    }

    public Borne getBorne() {
        return borne;
    }

    public void setBorne(Borne borne) {
        this.borne = borne;
    }
}
