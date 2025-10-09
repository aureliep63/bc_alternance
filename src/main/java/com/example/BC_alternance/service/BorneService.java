package com.example.BC_alternance.service;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;
import java.time.LocalDateTime;
import java.util.List;

public interface BorneService {

    List<BorneDto> getAllBornes();

    BorneDto getBorneById(Long id);

    List<BorneDto> getBornesByUserId(Long idUser);

    List<BorneDto> getBornesByReservationId(Long idResa);

    List<BorneDto> getBornesByMediaId(Long idMedia);

    Borne saveBorne(BorneDto borneDto);

    void deleteBorne(Long id);

    List<BorneDto> searchBornes(String ville, LocalDateTime dateDebut, LocalDateTime dateFin);

    boolean isBorneAvailable(Long borneId, LocalDateTime dateDebut, LocalDateTime dateFin);

    List<BorneDto> filterBornesByAvailability(List<BorneDto> bornes, LocalDateTime dateDebut, LocalDateTime dateFin);


    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}
