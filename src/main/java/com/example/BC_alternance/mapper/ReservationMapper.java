package com.example.BC_alternance.mapper;

import com.example.BC_alternance.dto.ReservationDto;
import com.example.BC_alternance.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {

    Reservation toEntity(ReservationDto reservationDto);

    @Mapping(target = "borneId", source = "borne.id")
    @Mapping(target = "utilisateurId", source = "utilisateur.id")
    ReservationDto toDto(Reservation reservation);
}
