package com.example.BC_alternance.service;

import com.example.BC_alternance.dto.ReservationDto;
import com.example.BC_alternance.model.Reservation;

import java.util.List;

public interface ReservationService {

    List<ReservationDto> getAllReservations();

    ReservationDto getReservationById(Long id);

    List<ReservationDto> getReservationByUserId(Long idUser);


    List<ReservationDto> getReservationsByBorneId(Long borneId);

    Reservation saveReservation(ReservationDto reservationDto);

    void deleteReservation(Long id);
}
