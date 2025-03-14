package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.LoginRequest;
import com.example.BC_alternance.dto.UtilisateurDto;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.service.UtilisateurService;
import com.example.BC_alternance.service.impl.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurRestController {


    private UtilisateurService utilisateurService;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public UtilisateurRestController(TokenService tokenService, UtilisateurService utilisateurService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

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
        utilisateurDto.setMotDePasse(passwordEncoder.encode(utilisateurDto.getMotDePasse()));
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


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authenticationObject = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword());
            authenticationObject = authenticationManager.authenticate(authenticationObject);

            String token = tokenService.generateToken(authenticationObject);

            //Créer un objet JSON avec le token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/current-user")
    public UtilisateurDto getUtilisateurCourant(@AuthenticationPrincipal UserDetails userDetails) {
        return utilisateurService.getUtilisateurByEmail(userDetails.getUsername());
    }
}
