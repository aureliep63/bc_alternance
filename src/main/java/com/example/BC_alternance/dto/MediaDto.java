package com.example.BC_alternance.dto;

import com.example.BC_alternance.model.enums.MediaTypeEnum;
import io.swagger.v3.oas.models.media.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class MediaDto {

    Long id;

    private String libelle;
    private MediaTypeEnum typeMedia;
    private Long borneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public MediaTypeEnum getTypeMedia() {
        return typeMedia;
    }

    public void setTypeMedia(MediaTypeEnum typeMedia) {
        this.typeMedia = typeMedia;
    }

    public Long getBorneId() {
        return borneId;
    }

    public void setBorneId(Long borneId) {
        this.borneId = borneId;
    }
}
