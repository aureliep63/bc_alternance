package com.example.BC_alternance.service;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;

import java.util.List;

public interface BorneService {

    List<BorneDto> getAllBornes();

    BorneDto getBorneById(Long id);

    List<BorneDto> getBornesByUserId(Long idUser);

    List<BorneDto> getBornesByReservationId(Long idResa);

    List<BorneDto> getBornesByMediaId(Long idMedia);

    Borne saveBorne(BorneDto borneDto);

    void deleteBorne(Long id);
}
