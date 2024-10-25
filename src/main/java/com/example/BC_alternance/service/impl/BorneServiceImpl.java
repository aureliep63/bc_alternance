package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.mapper.BorneMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.model.Media;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.LieuxRepository;
import com.example.BC_alternance.repository.MediaRepository;
import com.example.BC_alternance.repository.UtilisateurRepository;
import com.example.BC_alternance.service.BorneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorneServiceImpl implements BorneService {

    private BorneRepository borneRepository;
    private  MediaRepository mediaRepository;
    private UtilisateurRepository utilisateurRepository;
    private LieuxRepository lieuxRepository;

    public BorneServiceImpl(BorneRepository borneRepository, MediaRepository mediaRepository, UtilisateurRepository utilisateurRepository,LieuxRepository lieuxRepository){
        this.borneRepository = borneRepository;
        this.mediaRepository = mediaRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.lieuxRepository = lieuxRepository;
    }

    @Autowired
    private BorneMapper borneMapper;


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
    public Borne saveBorne(BorneDto borneDto) {
        Borne borne = borneMapper.toEntity(borneDto);
        if(borneDto.getMediasId() != null && borneDto.getMediasId().isEmpty()) {
            List<Media> medias = mediaRepository.findAllById(borneDto.getMediasId());
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
