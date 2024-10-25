package com.example.BC_alternance.mapper;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LieuxMapper {

    Lieux toEntity(LieuxDto lieuxDto);

    @Mapping(target = "bornesId", source="bornes")
    LieuxDto toDto(Lieux lieux);

    List<Long> map(List<Borne> bornes);

    default Long map(Borne borne){
        return borne != null ? borne.getId() : null;
    }
}
