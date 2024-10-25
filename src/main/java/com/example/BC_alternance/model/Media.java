package com.example.BC_alternance.model;

import com.example.BC_alternance.model.enums.MediaTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le choix du fichier est obligatoire.")
    private String libelle;

    @NotBlank(message = "Le choix du type de fichier est obligatoire.")
    @Enumerated(EnumType.STRING)
    private MediaTypeEnum typeMedia;

    @ManyToOne
    @NotBlank(message = "La borne est obligatoire.")
    @JsonIgnoreProperties({"medias"})
    private Borne borne;


}
