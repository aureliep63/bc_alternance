package com.example.BC_alternance.service;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;

import java.util.List;

public interface BorneService {

    List<BorneDto> getAllBornes();

    BorneDto getBorneById(Long id);

    Borne saveBorne(BorneDto borneDto);

    void deleteBorne(Long id);
}
