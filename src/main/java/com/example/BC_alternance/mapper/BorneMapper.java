package com.example.BC_alternance.mapper;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BorneMapper {

    Borne toEntity(BorneDto borneDto);

    @Mapping(target = "utilisateurId",source = "utilisateur.id")
    @Mapping(target = "lieuId",source="lieux.id")
    @Mapping(target = "mediasId", source="medias")
    BorneDto toDto(Borne borne);

    List<Long> map(List<Media> medias);

    default Long map(Media media){
        return media!=null?media.getId():null;
    }
}
