package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.mapper.BorneMapper;
import com.example.BC_alternance.model.*;
import com.example.BC_alternance.repository.*;
import com.example.BC_alternance.service.BorneService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorneServiceImpl implements BorneService {

    private BorneRepository borneRepository;
    private  MediaRepository mediaRepository;
    private UtilisateurRepository utilisateurRepository;
    private LieuxRepository lieuxRepository;
    private ReservationRepository reservationRepository;
    private BorneMapper borneMapper;

    public BorneServiceImpl(ReservationRepository reservationRepository, BorneRepository borneRepository, MediaRepository mediaRepository, UtilisateurRepository utilisateurRepository,LieuxRepository lieuxRepository, BorneMapper borneMapper){
        this.borneRepository = borneRepository;
        this.mediaRepository = mediaRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.lieuxRepository = lieuxRepository;
        this.reservationRepository = reservationRepository;
        this.borneMapper = borneMapper;
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
    public Borne saveBorne(BorneDto borneDto) {
        Borne borne = borneMapper.toEntity(borneDto);
        if(borneDto.getMediasId() != null && !borneDto.getMediasId().isEmpty()) {
            List<Media> medias = mediaRepository.findAllById(borneDto.getMediasId());
            borne.setMedias(medias);
        }
        if(borneDto.getReservationsId() != null && !borneDto.getReservationsId().isEmpty()) {
            List<Reservation> reservations = reservationRepository.findAllById(borneDto.getReservationsId());
        borne.setReservations(reservations);
        }
        if(borneDto.getUtilisateurId() != null){
            Utilisateur utilisateur= utilisateurRepository.findById(borneDto.getUtilisateurId()).orElse(null);
            borne.setUtilisateur(utilisateur);
        }
        if(borneDto.getLieuId() != null){
            Lieux lieux= lieuxRepository.findById(borneDto.getLieuId()).orElse(null);
            borne.setLieux(lieux);
        }
        return borneRepository.save(borne);
    }

    @Override
    public void deleteBorne(Long id) {
        borneRepository.deleteById(id);
    }

}
