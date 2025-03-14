package com.example.BC_alternance.service;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;

import java.util.List;

public interface BorneService {

    List<BorneDto> getAllBornes();

    BorneDto getBorneById(Long id);

    List<BorneDto> getBornesByUserId(Long idUser);

    Borne saveBorne(BorneDto borneDto);

    void deleteBorne(Long id);
}
