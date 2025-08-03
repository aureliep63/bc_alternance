package com.example.BC_alternance.service.impl;
import jakarta.persistence.EntityNotFoundException;
import com.example.BC_alternance.dto.BorneDto;

import com.example.BC_alternance.mapper.BorneMapper;
import com.example.BC_alternance.model.*;
import com.example.BC_alternance.repository.*;
import com.example.BC_alternance.service.BorneService;
import com.example.BC_alternance.service.LieuxService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class BorneServiceImpl implements BorneService {

    private BorneRepository borneRepository;
    private  MediaRepository mediaRepository;
    private UtilisateurRepository utilisateurRepository;
    private LieuxRepository lieuxRepository;
    private ReservationRepository reservationRepository;
    private BorneMapper borneMapper;
    private LieuxService lieuxService;

    public BorneServiceImpl(LieuxService lieuxService,ReservationRepository reservationRepository, BorneRepository borneRepository, MediaRepository mediaRepository, UtilisateurRepository utilisateurRepository,LieuxRepository lieuxRepository, BorneMapper borneMapper){
        this.borneRepository = borneRepository;
        this.mediaRepository = mediaRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.lieuxRepository = lieuxRepository;
        this.reservationRepository = reservationRepository;
        this.borneMapper = borneMapper;
        this.lieuxService = lieuxService;
    }

    private BorneDto borneDto;


    @Override
    public List<BorneDto> getAllBornes() {
        List<Borne> bornes = borneRepository.findAll();

        return bornes.stream()
                        .map(borneMapper::toDto)
                        .collect(Collectors.toList());
    }
    @Override
    public BorneDto getBorneById(Long id) {
        return borneRepository.findById(id)
                                .map(borneMapper::toDto)
                                .orElse(null);
    }

    @Override
    public List<BorneDto> getBornesByUserId(Long idUser){
        List<Borne> bornesUser = borneRepository.findByUtilisateurId(idUser);
        return bornesUser.stream()
                .map(borneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorneDto> getBornesByReservationId(Long idResa){
        List<Borne> bornesReservation = borneRepository.findByReservations_Id(idResa);
        return bornesReservation.stream()
                .map(borneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorneDto> getBornesByMediaId(Long idMedia){
        List<Borne> bornesMedia = borneRepository.findByMedias_Id(idMedia);
        return bornesMedia.stream()
                .map(borneMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Borne saveBorne(BorneDto borneDto) {
        Borne borne;

        // Gérer d'abord le cas d'une nouvelle borne
        if (borneDto.getId() == null || borneDto.getId() == 0) {
            borne = new Borne();
        } else {
            // Gérer le cas d'une mise à jour
            borne = borneRepository.findById(borneDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Borne non trouvée avec ID: " + borneDto.getId()));
        }

        // Mise à jour des champs communs via le mapper
        borneMapper.updateBorneFromDto(borneDto, borne);

        // Mettre à jour l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(borneDto.getUtilisateurId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec ID: " + borneDto.getUtilisateurId()));
        borne.setUtilisateur(utilisateur);

        // Mettre à jour le lieu
        Lieux lieu = lieuxRepository.findById(borneDto.getLieuId())
                .orElseThrow(() -> new EntityNotFoundException("Lieu non trouvé avec ID: " + borneDto.getLieuId()));
        borne.setLieux(lieu);

        // Gérer les autres relations...
        if(borneDto.getMediasId() != null && !borneDto.getMediasId().isEmpty()) {
            List<Media> medias = mediaRepository.findAllById(borneDto.getMediasId());
            borne.setMedias(medias);
        }
        if(borneDto.getReservationsId() != null && !borneDto.getReservationsId().isEmpty()) {
            List<Reservation> reservations = reservationRepository.findAllById(borneDto.getReservationsId());
            borne.setReservations(reservations);
        }

        return borneRepository.save(borne);
    }
//    @Override
//    public Borne saveBorne(BorneDto borneDto) {
//        Borne borne;
//
//        if (borneDto.getId() == null || borneDto.getId() == 0) {
//            // NOUVELLE création
//            borne = borneMapper.toEntity(borneDto);
//        } else {
//            // MISE À JOUR
//            Optional<Borne> existingOpt = borneRepository.findById(borneDto.getId());
//            if (existingOpt.isPresent()) {
//                Borne existingBorne = existingOpt.get();
//
//                // Mise à jour des champs simples depuis le DTO via mapper (implémente updateBorneFromDto dans BorneMapper)
//                borneMapper.updateBorneFromDto(borneDto, existingBorne);
//
//                borne = existingBorne;
//            } else {
//                throw new EntityNotFoundException("Borne non trouvée avec ID: " + borneDto.getId());
//            }
//        }
//
//        // Gestion obligatoire de l'utilisateur (relié à la borne)
//        Utilisateur utilisateur = utilisateurRepository.findById(borneDto.getUtilisateurId())
//                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec ID: " + borneDto.getUtilisateurId()));
//        borne.setUtilisateur(utilisateur);
//
//        // Gestion du lieu (relié à la borne)
//        if (borneDto.getLieuId() != null) {
//            Lieux lieu = lieuxRepository.findById(borneDto.getLieuId())
//                    .orElseThrow(() -> new EntityNotFoundException("Lieu non trouvé avec ID: " + borneDto.getLieuId()));
//            borne.setLieux(lieu);
//        } else {
//            borne.setLieux(null);
//        }
//
//        // gérer d'autres relations (medias, reservations...)
//        if(borneDto.getMediasId() != null && !borneDto.getMediasId().isEmpty()) {
//            List<Media> medias = mediaRepository.findAllById(borneDto.getMediasId());
//            borne.setMedias(medias);
//        }
//        if(borneDto.getReservationsId() != null && !borneDto.getReservationsId().isEmpty()) {
//            List<Reservation> reservations = reservationRepository.findAllById(borneDto.getReservationsId());
//        borne.setReservations(reservations);
//        }
//
//        return borneRepository.save(borne);
//    }

    @Override
    public void deleteBorne(Long id) {
        borneRepository.deleteById(id);
    }

    @Override
    public List<BorneDto> searchBornesDisponibles(String ville, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return borneRepository.findBornesDisponibles(
                ville != null && !ville.isBlank() ? ville : null,
                dateDebut,
                dateFin
        ).stream().map(borneMapper::toDto).collect(Collectors.toList());
    }

}
