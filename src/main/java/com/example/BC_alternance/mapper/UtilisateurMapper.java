package com.example.BC_alternance.mapper;

import com.example.BC_alternance.dto.UtilisateurDto;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UtilisateurMapper {

    Utilisateur toEntity(UtilisateurDto utilisateurDto);

    @Mapping(target = "bornesId", source = "bornes")
    @Mapping(target = "reservationsId", source = "reservations")
    UtilisateurDto toDto(Utilisateur utilisateur);

    List<Long> mapBornes(List<Borne> bornes);

    List<Long> mapReservations(List<Reservation> reservations);

    default Long map(Borne borne){
        return borne != null ? borne.getId() : null;
    }
    default  Long map(Reservation reservation){
        return reservation != null ? reservation.getId() : null;
    }
}
