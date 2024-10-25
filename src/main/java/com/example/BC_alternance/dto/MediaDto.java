package com.example.BC_alternance.dto;

import com.example.BC_alternance.model.enums.MediaTypeEnum;
import io.swagger.v3.oas.models.media.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDto {

    Long id;

    private String libelle;
    private MediaTypeEnum typeMedia;
    private Long borneId;
}
