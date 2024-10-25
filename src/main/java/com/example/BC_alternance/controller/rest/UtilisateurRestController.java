package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.UtilisateurDto;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurRestController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("")
    public List<UtilisateurDto> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/{id}")
    public UtilisateurDto getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Utilisateur saveUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
        return utilisateurService.saveUtilisateur(utilisateurDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
    }

    @PutMapping("/{id}")
    public Utilisateur updateUtilisateur(@PathVariable Long id, @RequestBody UtilisateurDto utilisateurDto) {
        utilisateurDto.setId(id);
        return utilisateurService.saveUtilisateur(utilisateurDto);
    }
}
