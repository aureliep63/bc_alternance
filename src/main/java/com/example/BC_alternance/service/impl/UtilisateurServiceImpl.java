package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.UtilisateurDto;
import com.example.BC_alternance.mapper.UtilisateurMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.ReservationRepository;
import com.example.BC_alternance.repository.UtilisateurRepository;
import com.example.BC_alternance.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private BorneRepository borneRepository;
    private ReservationRepository reservationRepository;
    private UtilisateurMapper utilisateurMapper;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, BorneRepository borneRepository,ReservationRepository reservationRepository,
                                  UtilisateurMapper utilisateurMapper){
        this.utilisateurRepository = utilisateurRepository;
        this.borneRepository = borneRepository;
        this.reservationRepository = reservationRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    private UtilisateurDto utilisateurDto;

    @Override
    public List<UtilisateurDto> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        return utilisateurs.stream().map(utilisateurMapper::toDto)
                            .collect(Collectors.toList());
    }

    @Override
    public UtilisateurDto getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                                        .map(utilisateurMapper::toDto)
                                        .orElse(null);
    }

    @Override
    public Utilisateur saveUtilisateur(UtilisateurDto utilisateurDto) {

        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDto);
        if(utilisateurDto.getBornesId() != null && !utilisateurDto.getBornesId().isEmpty()){
            List<Borne> bornes = borneRepository.findAllById(utilisateurDto.getBornesId());
            utilisateur.setBornes(bornes);
        }
        if(utilisateurDto.getReservationsId() != null && !utilisateurDto.getReservationsId().isEmpty()){
            List<Reservation> reservations = reservationRepository.findAllById(utilisateurDto.getReservationsId());
            utilisateur.setReservations(reservations);
        }
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public UtilisateurDto getUtilisateurByEmail(String email){
        return utilisateurRepository.findByEmail(email).map(utilisateurMapper::toDto).orElse(null);
    }

}
