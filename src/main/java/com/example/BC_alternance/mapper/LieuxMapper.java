package com.example.BC_alternance.mapper;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LieuxMapper {

    // Méthode pour la création (l'ID est généré par la BDD, donc on l'ignore ici)
    @Mapping(target = "id", ignore = true)
    Lieux toEntity(LieuxDto lieuxDto);

    @Mapping(target = "bornesId", source = "bornes")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    LieuxDto toDto(Lieux lieux);

    List<Long> map(List<Borne> bornes);

    default Long map(Borne borne){
        return borne != null ? borne.getId() : null;
    }

    @Mapping(target = "id", ignore = true)
    void updateLieuxFromDto(LieuxDto lieuxDto, @MappingTarget Lieux lieux);
}