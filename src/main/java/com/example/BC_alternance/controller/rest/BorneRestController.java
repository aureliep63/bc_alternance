package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.mapper.BorneMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.service.BorneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;

import jakarta.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bornes")
@Validated
public class BorneRestController {

    private final Path rootLocation = Paths.get("C:\\Users\\HB\\Desktop\\BC\\BC_alternance\\upload");
    private BorneService borneService;
    private BorneMapper borneMapper;
    public BorneRestController(BorneService borneService, BorneMapper borneMapper) {
        this.borneMapper = borneMapper;
        this.borneService = borneService;
    }

    @GetMapping("")
    public List<BorneDto> getAllBornes() {
        return borneService.getAllBornes();
    }

    @GetMapping("/{id}")
    public BorneDto getBorneById(@PathVariable Long id) {
        return borneService.getBorneById(id);
    }

//    @PostMapping("")
//    public ResponseEntity<BorneDto> saveBorne(@Valid @RequestBody BorneDto borneDto) {
//        Borne borne = borneService.saveBorne(borneDto);
//        BorneDto responseDto = borneMapper.toDto(borne);
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//    }
@Operation(summary = "Créer une nouvelle borne", description = "Créer une nouvelle borne avec son lieu existant ou non")
@PostMapping(value = "/user/bornes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@ResponseStatus(code = HttpStatus.CREATED)
public BorneDto saveBorne(
        @Valid @RequestParam("borneDto") String borneDTOJson,
        @RequestPart("file") MultipartFile file) throws Exception {

    ObjectMapper mapper = new ObjectMapper();
    BorneDto borneDTO = mapper.readValue(borneDTOJson, BorneDto.class);

    if (borneDTO.getLieuId() == null || borneDTO.getUtilisateurId() == null) {
        throw new IllegalArgumentException("Lieu ID et Utilisateur ID sont obligatoires.");
    }
    if (file.isEmpty()) {
        throw new Exception("Failed to store empty file.");
    }

    String nomFichier = UUID.randomUUID() + file.getOriginalFilename();
    Path destinationFile = this.rootLocation.resolve(
                    Paths.get(nomFichier))
            .normalize().toAbsolutePath();


    if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
        throw new Exception("Cannot store file outside current directory.");
    }

    try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
        throw new Exception("Failed to store file.", e);
    }

    borneDTO.setPhoto(nomFichier);
    Borne borne = borneService.saveBorne(borneDTO);
    return this.borneMapper.toDto(borne);
}

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBorne(@PathVariable Long id) {
        borneService.deleteBorne(id);
    }

//    @PutMapping("/{id}")
//    public Borne updateBorne(@PathVariable Long id,@Valid @RequestBody BorneDto borneDto) {
//        borneDto.setId(id);
//        return borneService.saveBorne(borneDto);
//    }
@Operation(summary = "Modifier une borne", description = "Modifier une borne et éventuellement sa photo")
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
        String nomFichier = UUID.randomUUID() + file.getOriginalFilename();
        Path destinationFile = this.rootLocation.resolve(
                        Paths.get(nomFichier))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new Exception("Cannot store file outside current directory.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new Exception("Failed to store file.", e);
        }

        borneDTO.setPhoto(nomFichier);
    }

    Borne updated = borneService.saveBorne(borneDTO);
    return this.borneMapper.toDto(updated);
}

    @GetMapping("/user/{idUser}/bornes")
    public List<BorneDto> getBornesByUser(@PathVariable Long idUser) {
        return borneService.getBornesByUserId(idUser);
    }

    @GetMapping("/reservation/{idResa}/bornes")
    public List<BorneDto> getBornesByReservation(@PathVariable Long idResa) {
        return borneService.getBornesByReservationId(idResa);
    }

    @GetMapping("/media/{idMedia}/bornes")
    public List<BorneDto> getBorneByMedia(@PathVariable Long idMedia) {
        return borneService.getBornesByMediaId(idMedia);
    }

    @GetMapping("/upload/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Path file = Paths.get("upload").resolve(filename);
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }


}

