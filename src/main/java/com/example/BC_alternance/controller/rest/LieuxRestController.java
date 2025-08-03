package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.mapper.LieuxMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.service.LieuxService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lieux")
@Validated
public class LieuxRestController {

    private LieuxService lieuxService;
    private LieuxMapper lieuxMapper;

    public LieuxRestController(LieuxService lieuxService, LieuxMapper lieuxMapper) {
        this.lieuxService = lieuxService;
        this.lieuxMapper = lieuxMapper;
    }

    @GetMapping("")
    @Operation(summary = "Affiche toutes les lieux", description = "Affiche toutes les lieux des bornes ")
    public List<LieuxDto> getAllLieux() {
        return lieuxService.getAllLieux();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Affiche un seul lieux", description = "Affiche un seul lieux par son ID")
    public LieuxDto getLieuxById(@PathVariable Long id) {
        return lieuxService.getLieuxById(id);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Ajoute un nouveau lieux", description = "Ajoute un nouveau lieux")
    public LieuxDto saveLieux(@Valid @RequestBody LieuxDto lieuxDto, BindingResult bindingResult) {
       Lieux lieux = lieuxService.saveLieux(lieuxDto);
        return this.lieuxMapper.toDto(lieux);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprime un lieux", description = "Supprime un lieux par son ID")
    public void deleteLieux(@PathVariable Long id) {
    lieuxService.deleteLieux(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifie un lieux", description = "Modifie un lieux par son ID")
    public LieuxDto updateLieux(@PathVariable Long id,@Valid @RequestBody LieuxDto lieuxDto, BindingResult bindingResult) {
        lieuxDto.setId(id);
        Lieux lieuxUpdate = lieuxService.saveLieux(lieuxDto);
        return this.lieuxMapper.toDto(lieuxUpdate);
    }

    @PostMapping("/update-coords")
    public ResponseEntity<String> updateCoords() {
        lieuxService.updateCoordinatesForAllLieuxWithoutCoords();
        return ResponseEntity.ok("Mise à jour des coordonnées lancée");
    }
}