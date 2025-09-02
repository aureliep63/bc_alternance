package com.example.BC_alternance.controller.rest;


import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.dto.SearchRequest;
import com.example.BC_alternance.mapper.BorneMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.service.BorneService;
import com.example.BC_alternance.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/bornes")
public class BorneRestController {

    private BorneService borneService;
    private BorneMapper borneMapper;
    private StorageService storageService;

    public BorneRestController(BorneService borneService, BorneMapper borneMapper, StorageService storageService) {
        this.borneMapper = borneMapper;
        this.borneService = borneService;
        this.storageService = storageService;
    }

    // ...

    @GetMapping("")
    public List<BorneDto> getAllBornes() {
        return borneService.getAllBornes();
    }

    @GetMapping("/{id}")
    public BorneDto getBorneById(@PathVariable Long id) {
        return borneService.getBorneById(id);
    }

    @Operation(summary = "Créer une nouvelle borne", description = "Créer une nouvelle borne avec son user")
    @PostMapping(value = "/user/bornes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public BorneDto saveBorne(@Valid @RequestParam("borneDto") String borneDTOJson, @RequestPart("file") MultipartFile file) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        BorneDto borneDTO = mapper.readValue(borneDTOJson, BorneDto.class);

        if (borneDTO.getLieuId() == null || borneDTO.getUtilisateurId() == null) {
            throw new IllegalArgumentException("Lieu ID et Utilisateur ID sont obligatoires.");
        }

        // Déléguer la sauvegarde du fichier au service
        String nomFichier = storageService.store(file);
        borneDTO.setPhoto(nomFichier);

        Borne borne = borneService.saveBorne(borneDTO);
        return this.borneMapper.toDto(borne);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprime une borne", description = "Supprime une borne par son ID")
    public void deleteBorne(@PathVariable Long id) {
        borneService.deleteBorne(id);
    }

    @Operation(summary = "Modifier une borne", description = "Modifier une borne par son ID")
    @PutMapping(value = "/user/bornes/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BorneDto updateBorne(
            @PathVariable Long id,
            @Valid @RequestParam("borneDto") String borneDTOJson,
            @RequestPart(name = "file", required = false) MultipartFile file) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        BorneDto borneDTO = mapper.readValue(borneDTOJson, BorneDto.class);
        borneDTO.setId(id);

        if (borneDTO.getLieuId() == null || borneDTO.getUtilisateurId() == null) {
            throw new IllegalArgumentException("Lieu ID et Utilisateur ID sont obligatoires.");
        }

        if (file != null && !file.isEmpty()) {
            // Déléguer la sauvegarde du fichier au service
            String nomFichier = storageService.store(file);
            borneDTO.setPhoto(nomFichier);
        }

        Borne updated = borneService.saveBorne(borneDTO);
        return this.borneMapper.toDto(updated);
    }

    @GetMapping("/user/{idUser}/bornes")
    @Operation(summary = "Affiche les bornes d'un user", description = "Affiche les bornes d'un user par l'ID du User")
    public List<BorneDto> getBornesByUser(@PathVariable Long idUser) {
        return borneService.getBornesByUserId(idUser);
    }

    @GetMapping("/reservation/{idResa}/bornes")
    @Operation(summary = "Affiche la borne d'une reservation", description = "Affiche la borne d'une réservation par l'ID de la réservation ")
    public List<BorneDto> getBornesByReservation(@PathVariable Long idResa) {
        return borneService.getBornesByReservationId(idResa);
    }

    @GetMapping("/upload/{filename}")
    @Operation(summary = "Endpoint pour une image upload ")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource resource = new FileSystemResource(storageService.load(filename));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }




    @Operation(summary = "Recherche de borne par des filtres", description = "Recherche de borne par ville, rayon et/ou date-heure")
    @PostMapping("/search")
    public ResponseEntity<List<BorneDto>> searchBornes(@RequestBody SearchRequest request) {
        List<BorneDto> resultats = borneService.searchBornes(
                request.getVille(), request.getDateDebut(), request.getDateFin()
        );
        return ResponseEntity.ok(resultats);
    }

}
