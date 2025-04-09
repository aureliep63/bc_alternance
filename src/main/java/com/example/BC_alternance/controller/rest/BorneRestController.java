package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.mapper.BorneMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.service.BorneService;
import org.springframework.core.io.Resource;

import jakarta.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@RestController
@RequestMapping("/bornes")
@Validated
public class BorneRestController {

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

    @PostMapping("")
    public ResponseEntity<BorneDto> saveBorne(@Valid @RequestBody BorneDto borneDto) {
        Borne borne = borneService.saveBorne(borneDto);
        BorneDto responseDto = borneMapper.toDto(borne);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBorne(@PathVariable Long id) {
        borneService.deleteBorne(id);
    }

    @PutMapping("/{id}")
    public Borne updateBorne(@PathVariable Long id,@Valid @RequestBody BorneDto borneDto) {
        borneDto.setId(id);
        return borneService.saveBorne(borneDto);
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

