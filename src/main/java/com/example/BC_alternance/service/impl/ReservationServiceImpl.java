package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.ReservationDto;
import com.example.BC_alternance.mapper.ReservationMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.ReservationRepository;
import com.example.BC_alternance.repository.UtilisateurRepository;
import com.example.BC_alternance.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private UtilisateurRepository utilisateurRepository;
    private BorneRepository borneRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,UtilisateurRepository utilisateurRepository,BorneRepository borneRepository){
        this.reservationRepository = reservationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.borneRepository = borneRepository;
    }

    private ReservationDto reservationDto;

    @Autowired
    private ReservationMapper reservationMapper;


    @Override
    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                            .map(reservationMapper::toDto)
                            .collect(Collectors.toList());
    }

    @Override
    public ReservationDto getReservationById(Long id) {
        return reservationRepository.findById(id)
                                        .map(reservationMapper::toDto)
                                        .orElse(null);
    }

    @Override
    public Reservation saveReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.toEntity(reservationDto);
        if(reservationDto.getUtilisateurId() != null){
            Utilisateur utilisateur = utilisateurRepository.findById(reservationDto.getUtilisateurId()).orElse(null);
            reservation.setUtilisateur(utilisateur);
        }
        if (reservationDto.getBorneId() != null){
            Borne borne = borneRepository.findById(reservationDto.getBorneId()).orElse(null);
            reservation.setBorne(borne);
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
