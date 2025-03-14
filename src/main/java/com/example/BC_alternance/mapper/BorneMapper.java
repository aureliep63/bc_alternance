package com.example.BC_alternance.mapper;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Media;
import com.example.BC_alternance.model.Reservation;
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
    @Mapping(target = "reservationsId", source="reservations")
    BorneDto toDto(Borne borne);

    List<Long> mapMedia(List<Media> medias);
    List<Long> mapReservation(List<Reservation> reservations);

    default Long map(Media media){
        return media!=null?media.getId():null;
    }
    default Long map(Reservation reservation){
        return reservation!=null?reservation.getId():null;
    }
}
