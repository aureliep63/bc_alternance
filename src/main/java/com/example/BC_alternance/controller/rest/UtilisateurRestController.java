package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.LoginRequest;
import com.example.BC_alternance.dto.UtilisateurDto;
import com.example.BC_alternance.mapper.UtilisateurMapper;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.model.enums.RolesEnum;
import com.example.BC_alternance.repository.UtilisateurRepository;
import com.example.BC_alternance.service.EmailService;
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
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private UtilisateurMapper utilisateurMapper;
    private final UtilisateurRepository utilisateurRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private EmailService emailService;


    public UtilisateurRestController(CustomUserDetailsService customUserDetailsService,TokenService tokenService, UtilisateurService utilisateurService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UtilisateurMapper utilisateurMapper,
                                     UtilisateurRepository utilisateurRepository, EmailService emailService) {
        this.tokenService = tokenService;
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.utilisateurMapper = utilisateurMapper;
        this.utilisateurRepository = utilisateurRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.emailService = emailService;
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

//    @PostMapping("")
//    @ResponseStatus(code = HttpStatus.CREATED)
//    @Operation(summary = "Ajoute un nouvel utilisateur", description = "Ajoute un nouvel utilisateur")
//    public UtilisateurDto saveUtilisateur(@RequestBody @Valid UtilisateurDto utilisateurDto, BindingResult bindingResult) {
//        utilisateurDto.setMotDePasse(passwordEncoder.encode(utilisateurDto.getMotDePasse()));
//
//        Utilisateur utilisateur = utilisateurService.saveUtilisateur(utilisateurDto);
//        return this.utilisateurMapper.toDto(utilisateur);
//    }

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


    @Operation(summary = "Inscription d'un utilisateur", description = "Inscription d'un utilisateur avec ses informations personnelles")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Utilisateur user) {
        if (utilisateurRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("L'email est déjà utilisé !");
        }

        // Hacher le mot de passe avant de sauvegarder
        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
        // Générer un token de validation
        String validationToken = UUID.randomUUID().toString(); // Pour un vrai projet, utilisez JWT
        user.setValidationToken(validationToken);
        user.setEnabled(false); // Le compte n'est pas activé tant que l'email n'est pas validé
    user.setRole(RolesEnum.PROPRIO_LOCATAIRE);
        utilisateurRepository.save(user);

        // Construire le lien de validation qui pointe vers votre frontend Angular
        String validationLink = "http://localhost:4200/validate-email?token=" + validationToken; // Adaptez le port Angular
        emailService.sendValidationEmail(user.getEmail(), validationLink);

        return ResponseEntity.ok(Map.of( "message","Inscription réussie ! Un e-mail de validation a été envoyé."));
    }

    // Mapping pour la validation de l'e-mail
    @GetMapping("/validate-email")
    public ResponseEntity<?> validateEmail(@RequestParam String token) {
        Optional<Utilisateur> userOptional = utilisateurRepository.findByValidationToken(token);

        if (userOptional.isPresent()) {
            Utilisateur user = userOptional.get();
            user.setEnabled(true); // Activer le compte
            user.setValidationToken(null); // Supprimer le token après validation
            utilisateurRepository.save(user);
            // Rediriger l'utilisateur vers une page de succès sur le frontend Angular
            // Ou retourner un message de succès
            return ResponseEntity.ok(Map.of( "message","Votre compte a été validé avec succès !"));
        } else {
            return ResponseEntity.badRequest().body("Token de validation invalide ou expiré.");
        }
    }

    @Operation(summary = "Connexion via Google/Firebase", description = "Connexion ou inscription d’un utilisateur via Google/Firebase")
    @PostMapping("/firebase-login")
    public ResponseEntity<Map<String, String>> firebaseLogin(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("idToken");

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();

            UtilisateurDto utilisateur = utilisateurService.findOrCreateFromFirebase(email);

            // ⚠️ Génère l'objet Authentication requis
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(utilisateur.getEmail());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            String jwtToken = tokenService.generateToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);
            return ResponseEntity.ok(response);

        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}









