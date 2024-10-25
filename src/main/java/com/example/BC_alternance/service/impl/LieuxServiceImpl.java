package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.mapper.LieuxMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.LieuxRepository;
import com.example.BC_alternance.service.BorneService;
import com.example.BC_alternance.service.LieuxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LieuxServiceImpl implements LieuxService {

    private LieuxRepository lieuxRepository;
    private BorneRepository borneRepository;

    public LieuxServiceImpl(LieuxRepository lieuxRepository, BorneRepository borneRepository){
        this.lieuxRepository = lieuxRepository;
        this.borneRepository = borneRepository;
    }


    private LieuxDto lieuxDto;

    @Autowired
    private LieuxMapper lieuxMapper;



    @Override
    public List<LieuxDto> getAllLieux() {
        List<Lieux> lieux = lieuxRepository.findAll();
        return lieux.stream()
                    .map(lieuxMapper::toDto)
                    .collect(Collectors.toList());
    }

    @Override
    public LieuxDto getLieuxById(Long id) {
        return lieuxRepository.findById(id)
                                .map(lieuxMapper::toDto)
                .orElse(null);
    }

    @Override
    public Lieux saveLieux(LieuxDto lieuxDto) {
        Lieux lieux = lieuxMapper.toEntity(lieuxDto);
        if(lieuxDto.getBornesId() != null && lieuxDto.getBornesId().isEmpty()) {
            List<Borne> bornes = borneRepository.findAllById(lieuxDto.getBornesId());
            lieux.setBornes(bornes);
        }
        return lieuxRepository.save(lieux);
    }

    @Override
    public void deleteLieux(Long id) {
        lieuxRepository.deleteById(id);
    }
}
