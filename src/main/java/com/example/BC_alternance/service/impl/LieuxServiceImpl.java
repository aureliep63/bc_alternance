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
        if (lieuxDto.getId() == null || lieuxDto.getId() == 0) {
            // C'est une NOUVELLE création
            lieux = lieuxMapper.toEntity(lieuxDto); // Utilise la nouvelle méthode qui ignore l'ID
        } else {
            // C'est une MISE À JOUR d'un lieu existant
            // Il faut d'abord récupérer l'entité existante, puis la mettre à jour
            Lieux existingLieux = lieuxRepository.findById(lieuxDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Lieu non trouvé pour la mise à jour avec ID: " + lieuxDto.getId()));
            lieuxMapper.updateLieuxFromDto(lieuxDto, existingLieux); // Créer cette méthode de mise à jour dans le mapper
            lieux = existingLieux;
        }

        // Géocodage (le même code)
        String fullAddress = lieux.getAdresse() + ", " + lieux.getVille() + ", " + lieux.getCodePostal();
        GeocodingService.LatLng coords = geocodingService.geocodeAddress(fullAddress);
        if (coords != null) {
            lieux.setLatitude(coords.lat);
            lieux.setLongitude(coords.lon);
        }

        if (lieuxDto.getBornesId() != null && !lieuxDto.getBornesId().isEmpty()) {
            List<Borne> bornes = borneRepository.findAllById(lieuxDto.getBornesId());
            lieux.setBornes(bornes);
        }
        return lieuxRepository.save(lieux); // Maintenant, save() fonctionnera correctement
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
