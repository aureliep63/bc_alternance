package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.LoginRequest;
import com.example.BC_alternance.dto.UtilisateurDto;
import com.example.BC_alternance.mapper.UtilisateurMapper;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.repository.UtilisateurRepository;
import com.example.BC_alternance.service.UtilisateurService;
import com.example.BC_alternance.service.impl.CustomUserDetailsService;
import com.example.BC_alternance.service.impl.TokenService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.*;

@RestController
@RequestMapping("/utilisateurs")
@Validated
public class UtilisateurRestController {

    private UtilisateurService utilisateurService;
    private TokenService tokenService;
    private AuthenticationManager authenticationManager;
    private UtilisateurMapper utilisateurMapper;
    private final UtilisateurRepository utilisateurRepository;
    private final CustomUserDetailsService customUserDetailsService;



    public UtilisateurRestController(CustomUserDetailsService customUserDetailsService,TokenService tokenService, UtilisateurService utilisateurService, AuthenticationManager authenticationManager, UtilisateurMapper utilisateurMapper,
                                     UtilisateurRepository utilisateurRepository) {
        this.tokenService = tokenService;
        this.utilisateurService = utilisateurService;
        this.authenticationManager = authenticationManager;
        this.utilisateurMapper = utilisateurMapper;
        this.utilisateurRepository = utilisateurRepository;
        this.customUserDetailsService = customUserDetailsService;

    }

    @GetMapping("")
    @Operation(summary = "Affiche touts les utilisateurs", description = "Affiche touts les utilisateurs ")
    public List<UtilisateurDto> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Affiche un utilisateur", description = "Affiche un utilisateurs par son ID")
    public UtilisateurDto getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprime un utilisateur", description = "Supprime un utilisateur par son ID")
    public void deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifie un utilisateur", description = "Modifie un utilisateur par son ID")
    public UtilisateurDto updateUtilisateur(@PathVariable Long id, @RequestBody @Valid UtilisateurDto utilisateurDto , BindingResult bindingResult) {
        utilisateurDto.setId(id);
       Utilisateur utilisateurUpdate = utilisateurService.saveUtilisateur(utilisateurDto);
        return this.utilisateurMapper.toDto(utilisateurUpdate);
    }


    @PostMapping("/login")
    @Operation(summary = "Connection d'un utilisateur", description = "Connection d'un utilisateur avec son email et son mot de passe")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequest loginRequest , BindingResult bindingResult) {
        try {
            // Récupère l'user dans la base
            Utilisateur utilisateur = utilisateurRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Utilisateur inconnu"));

            // Vérifie si email est validé
            if (!utilisateur.isEmailValidated()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Vous devez valider votre email avant de vous connecter."));
            }

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
    @Operation(summary = "Affiche l'utilisateur courant", description = "Affiche l'utilisateur courant")
    public UtilisateurDto getUtilisateurCourant(@AuthenticationPrincipal UserDetails userDetails) {
        return utilisateurService.getUtilisateurByEmail(userDetails.getUsername());
    }


    @Operation(summary = "Inscription d'un utilisateur", description = "Inscrit un utilisateur en générant un code de validation et en l'envoyant par e-mail.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UtilisateurDto utilisateurDto) {
        try {
            utilisateurService.registerUser(utilisateurDto);
            return ResponseEntity.ok(Map.of("message", "Inscription réussie ! Un code de validation a été envoyé à votre e-mail."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Validation de l'e-mail", description = "Valide l'adresse e-mail de l'utilisateur avec le code reçu.")
    @PostMapping("/validate-email")
    public ResponseEntity<?> validateEmail(@RequestParam String email, @RequestParam String code) {
        try {
            if (utilisateurService.validateEmail(email, code)) {
                return ResponseEntity.ok(Map.of("message", "Votre compte a été validé avec succès !"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Le code est invalide."));
    }


    @Operation(summary = "Renvoyer un code de validation", description = "Génère et envoie un nouveau code de validation à l'utilisateur.")
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendCode(@RequestParam String email) {
        try {
            utilisateurService.resendValidationCode(email);
            return ResponseEntity.ok(Map.of("message", "Un nouveau code a été envoyé à votre e-mail."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        boolean exists = utilisateurService.checkEmailExists(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @Operation(summary = "Connexion via Google/Firebase", description = "Connexion ou inscription d’un utilisateur via Google/Firebase")
    @PostMapping("/firebase-login")
    public ResponseEntity<Map<String, Object>> firebaseLogin(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("idToken");

        if (idToken == null || idToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token manquant"));
        }
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();
            System.out.println(" Email vérifié par Firebase: " + email);
            UtilisateurDto utilisateur = utilisateurService.findOrCreateFromFirebase(email);

            //  Génère l'objet Authentication requis
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(utilisateur.getEmail());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            String jwtToken = tokenService.generateToken(authentication);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("user", utilisateur);
            return ResponseEntity.ok(response);

        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("/unverified/{email}")
    @Operation(summary = "Supprime un utilisateur non validé par email")
    public void deleteUnverifiedUser(@PathVariable String email) {
        utilisateurRepository.findByEmail(email)
                .filter(u -> !u.isEmailValidated())  // supprimer uniquement si non validé
                .ifPresent(utilisateurRepository::delete);
    }

}









