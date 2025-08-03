package com.example.BC_alternance.service;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.model.Reservation;

import java.util.List;

public interface LieuxService {

    List<LieuxDto> getAllLieux();

    LieuxDto getLieuxById(Long id);

    Lieux saveLieux(LieuxDto lieuxDto);

    void deleteLieux(Long id);

    void updateCoordinatesForAllLieuxWithoutCoords();
}
