package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.mapper.LieuxMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.LieuxRepository;
import com.example.BC_alternance.service.GeocodingService;
import com.example.BC_alternance.service.LieuxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;

@Service
public class LieuxServiceImpl implements LieuxService {

    private LieuxRepository lieuxRepository;
    private BorneRepository borneRepository;
    private LieuxMapper lieuxMapper;
    private final GeocodingService geocodingService;


    public LieuxServiceImpl(LieuxRepository lieuxRepository, BorneRepository borneRepository, LieuxMapper lieuxMapper, GeocodingService geocodingService){
        this.lieuxRepository = lieuxRepository;
        this.borneRepository = borneRepository;
        this.lieuxMapper = lieuxMapper;
        this.geocodingService = geocodingService;

    }

    private LieuxDto lieuxDto;



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
        Lieux lieux;
        boolean isNewLieux = (lieuxDto.getId() == null || lieuxDto.getId() == 0); // Détecter si c'est une création

        if (isNewLieux) {
            // C'est une NOUVELLE création
            lieux = lieuxMapper.toEntity(lieuxDto);
        } else {
            // C'est une MISE À JOUR d'un lieu existant
            Lieux existingLieux = lieuxRepository.findById(lieuxDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Lieu non trouvé pour la mise à jour avec ID: " + lieuxDto.getId()));
            lieuxMapper.updateLieuxFromDto(lieuxDto, existingLieux);
            lieux = existingLieux;
        }

        // 1. Géocodage
        String fullAddress = lieux.getAdresse() + ", " + lieux.getVille() + ", " + lieux.getCodePostal();
        GeocodingService.LatLng coords = geocodingService.geocodeAddress(fullAddress);

        // 2. VÉRIFICATION STRICTE 🚨
        if (coords == null) {
            // Si les coordonnées ne sont pas trouvées
            // et que c'est une nouvelle création (ou si les coordonnées sont manquantes même en update),
            // Levez une exception qui sera gérée par le contrôleur et renverra une erreur 400 au client.
            throw new IllegalArgumentException("Impossible de trouver des coordonnées valides pour l'adresse fournie : " + fullAddress);
        }

        // 3. Mise à jour des coordonnées si le géocodage est réussi
        lieux.setLatitude(coords.lat);
        lieux.setLongitude(coords.lon);

        if (lieuxDto.getBornesId() != null && !lieuxDto.getBornesId().isEmpty()) {
            List<Borne> bornes = borneRepository.findAllById(lieuxDto.getBornesId());
            lieux.setBornes(bornes);
        }

        return lieuxRepository.save(lieux);
    }


    @Override
    public void deleteLieux(Long id) {
        lieuxRepository.deleteById(id);
    }

   @Override
    public void updateCoordinatesForAllLieuxWithoutCoords() {
        List<Lieux> lieuxSansCoords = lieuxRepository.findByLatitudeIsNullOrLongitudeIsNull();

        for (Lieux lieux : lieuxSansCoords) {
            String fullAddress = lieux.getAdresse() + ", " + lieux.getVille();
            GeocodingService.LatLng coords = geocodingService.geocodeAddress(fullAddress);

            if (coords != null) {
                lieux.setLatitude(coords.lat);
                lieux.setLongitude(coords.lon);
                lieuxRepository.save(lieux);
                System.out.println("Mis à jour coords pour " + fullAddress);
            } else {
                System.out.println("Impossible de géocoder : " + fullAddress);
            }

            // Respecte les règles d'utilisation (pause entre requêtes)
            try {
                Thread.sleep(1000); // 1 seconde entre requêtes (Nominatim recommande de ne pas surcharger)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
