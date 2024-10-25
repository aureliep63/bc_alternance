package com.example.BC_alternance.mapper;

import com.example.BC_alternance.dto.MediaDto;
import com.example.BC_alternance.model.Media;
import com.example.BC_alternance.model.enums.MediaTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper {

    Media toEntity(MediaDto mediaDto);

    @Mapping(target = "borneId", source = "borne.id")
    MediaDto toDto(Media media);


}
