package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.ReservationDto;
import com.example.BC_alternance.mapper.ReservationMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.model.enums.StatusEnum;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.ReservationRepository;
import com.example.BC_alternance.repository.UtilisateurRepository;
import com.example.BC_alternance.service.ReservationService;
import org.hibernate.engine.spi.Status;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private UtilisateurRepository utilisateurRepository;
    private BorneRepository borneRepository;
    private ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository,UtilisateurRepository utilisateurRepository,BorneRepository borneRepository, ReservationMapper reservationMapper){
        this.reservationRepository = reservationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.borneRepository = borneRepository;
        this.reservationMapper = reservationMapper;
    }

    private ReservationDto reservationDto;

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
    public List<ReservationDto> getReservationByUserId(Long idUser){
        List<Reservation> reservationUser = reservationRepository.findByUtilisateurId(idUser);
        return reservationUser.stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> getReservationsByBorneId(Long borneId){
        List<Reservation> reservationBorne = reservationRepository.findByBorneId(borneId);
        return reservationBorne.stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation saveReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.toEntity(reservationDto);
        Utilisateur utilisateur = null;
        Borne borne = null;

        // Récupère user
        if(reservationDto.getUtilisateurId() != null){
             utilisateur = utilisateurRepository.findById(
                    reservationDto.getUtilisateurId()).orElse(null);
            reservation.setUtilisateur(utilisateur);
        }
        // Récupère Borne
        if (reservationDto.getBorneId() != null){
             borne = borneRepository.findById(reservationDto.getBorneId()).orElse(null);
            reservation.setBorne(borne);
        }

        // User ne peut pas réserver sa borne
        if (borne != null && utilisateur != null &&
                borne.getUtilisateur() != null &&
                borne.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new IllegalArgumentException("Vous ne pouvez pas réserver votre propre borne.");
        }

        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

   @Override
    public void updateStatus(Long id, String status) {
        Reservation resa = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Réservation non trouvée"));
        try {
            StatusEnum enumStatus = StatusEnum.valueOf(status);
            resa.setStatus(enumStatus);
            reservationRepository.save(resa);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Statut invalide : " + status);
        }
    }


    @Override
    public boolean isBorneAvailable(Long borneId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        // Logique de vérification du chevauchement
        // Récupérer toutes les réservations d'une borne
        List<Reservation> existingReservations = reservationRepository.findByBorneId(borneId);

        // Parcourir les réservations existantes pour vérifier un chevauchement
        for (Reservation reservation : existingReservations) {
            // Un chevauchement existe si :
            // (1) La nouvelle réservation commence avant que l'existante ne se termine
            // AND
            // (2) La nouvelle réservation se termine après que l'existante ait commencé
            boolean overlap = (dateDebut.isBefore(reservation.getDateFin()) || dateDebut.isEqual(reservation.getDateFin())) &&
                    (dateFin.isAfter(reservation.getDateDebut()) || dateFin.isEqual(reservation.getDateDebut()));

            if (overlap) {
                return false; // La borne n'est pas disponible
            }
        }
        return true; // Aucune réservation ne se chevauche, la borne est disponible
    }
}
