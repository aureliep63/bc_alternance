package com.example.BC_alternance.service.impl;
import com.example.BC_alternance.dto.SearchRequest;
import com.example.BC_alternance.service.GeocodingService;
import com.example.BC_alternance.service.ReservationService;
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
import java.util.stream.Collectors;
@Service
public class BorneServiceImpl implements BorneService {
    //...

    private BorneRepository borneRepository;
    private  MediaRepository mediaRepository;
    private UtilisateurRepository utilisateurRepository;
    private LieuxRepository lieuxRepository;
    private ReservationRepository reservationRepository;
    private BorneMapper borneMapper;
    private LieuxService lieuxService;
    private final GeocodingService geocodingService;
    private final ReservationService reservationService;
    private SearchRequest searchRequest ;

    public BorneServiceImpl(ReservationService reservationService,GeocodingService geocodingService, LieuxService lieuxService,ReservationRepository reservationRepository, BorneRepository borneRepository, MediaRepository mediaRepository, UtilisateurRepository utilisateurRepository,LieuxRepository lieuxRepository, BorneMapper borneMapper){
        this.reservationService = reservationService;
        this.geocodingService = geocodingService;
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


    @Override
    public void deleteBorne(Long id) {
        borneRepository.deleteById(id);
    }





    @Override
    public List<BorneDto> searchBornes(String ville, LocalDateTime dateDebut, LocalDateTime dateFin) {
        List<Borne> bornes;
        if (ville != null && !ville.isBlank()) {
            String villePattern = "%" + ville.toLowerCase() + "%"; // Vous aviez cette ligne, c'est bien.
            if (dateDebut != null && dateFin != null) {
                // Cas : ville + dates
                bornes = borneRepository.findBornesWithVilleAndDateRange(villePattern, dateDebut, dateFin);
            } else {
                // Cas : ville seule
                bornes = borneRepository.findByLieuxVilleStartingWithIgnoreCase(ville);
            }
        } else if (dateDebut != null && dateFin != null) {
            // Cas : dates seules
            bornes = borneRepository.findAvailableBornesByDateRange(dateDebut, dateFin);
        } else {
            // Cas : aucun filtre
            bornes = borneRepository.findAll();
        }

        return bornes.stream()
                .map(borneMapper::toDto)
                .collect(Collectors.toList());
    }

    // Méthode utilitaire pour le filtrage par rayon
    private boolean isWithinRadius(Borne borne, Double centerLat, Double centerLon, Double radiusKm) {
        if (borne.getLieux() == null || borne.getLieux().getLatitude() == null || borne.getLieux().getLongitude() == null) {
            return false;
        }

        double R = 6371; // Rayon de la Terre en km
        double lat1 = Math.toRadians(centerLat);
        double lon1 = Math.toRadians(centerLon);
        double lat2 = Math.toRadians(borne.getLieux().getLatitude());
        double lon2 = Math.toRadians(borne.getLieux().getLongitude());

        double dLon = lon2 - lon1;
        double dLat = lat2 - lat1;

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance <= radiusKm;
    }



    @Override
    public boolean isBorneAvailable(Long borneId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        // Implement the logic here.
        // Check the database for any reservations for the given borne that overlap with the requested time range.
        // Example: reservationRepository.findOverlappingReservations(borneId, dateDebut, dateFin);
        return true; // Replace with actual logic
    }

    @Override
    public List<BorneDto> filterBornesByAvailability(List<BorneDto> bornes, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return bornes.stream()
                .filter(borne -> reservationService.isBorneAvailable(borne.getId(), dateDebut, dateFin))
                .collect(Collectors.toList());
}



    @Override
  public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Rayon de la Terre en km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


}
