package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.config.CodeGenerator;
import com.example.BC_alternance.dto.UtilisateurDto;
import com.example.BC_alternance.mapper.UtilisateurMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.model.enums.RolesEnum;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.ReservationRepository;
import com.example.BC_alternance.repository.UtilisateurRepository;
import com.example.BC_alternance.service.EmailService;
import com.example.BC_alternance.service.UtilisateurService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private BorneRepository borneRepository;
    private ReservationRepository reservationRepository;
    private UtilisateurMapper utilisateurMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurServiceImpl( EmailService emailService,
                                   PasswordEncoder passwordEncoder,UtilisateurRepository utilisateurRepository, BorneRepository borneRepository,ReservationRepository reservationRepository,
                                  UtilisateurMapper utilisateurMapper){
        this.utilisateurRepository = utilisateurRepository;
        this.borneRepository = borneRepository;
        this.reservationRepository = reservationRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    private UtilisateurDto utilisateurDto;

    @Override
    public List<UtilisateurDto> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        return utilisateurs.stream().map(utilisateurMapper::toDto)
                            .collect(Collectors.toList());
    }

    @Override
    public UtilisateurDto getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                                        .map(utilisateurMapper::toDto)
                                        .orElse(null);
    }


    @Override
    public Utilisateur saveUtilisateur(UtilisateurDto utilisateurDto) {
        System.out.println("Attempting to save user in service: " + utilisateurDto.getEmail());
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDto);

        if(utilisateurDto.getBornesId() != null && !utilisateurDto.getBornesId().isEmpty()){
            List<Borne> bornes = borneRepository.findAllById(utilisateurDto.getBornesId());
            utilisateur.setBornes(bornes);
        }
        if(utilisateurDto.getReservationsId() != null && !utilisateurDto.getReservationsId().isEmpty()){
            List<Reservation> reservations = reservationRepository.findAllById(utilisateurDto.getReservationsId());
            utilisateur.setReservations(reservations);
        }
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        System.out.println("User saved to database with ID: " + savedUtilisateur.getId());
        return savedUtilisateur;
    }


    @Override
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public UtilisateurDto getUtilisateurByEmail(String email){
        return utilisateurRepository.findByEmail(email).map(utilisateurMapper::toDto).orElse(null);
    }

    @Override
    public UtilisateurDto findOrCreateFromFirebase(String email) {
        // Vérifie si l'utilisateur existe déjà
        UtilisateurDto existingUser = getUtilisateurByEmail(email);
        if (existingUser != null) {
            return existingUser;
        }

        // Si l'utilisateur n'existe pas, crée un utilisateur minimal
        UtilisateurDto newUser = new UtilisateurDto();
        newUser.setEmail(email);
        newUser.setMotDePasse(""); // Pas de mot de passe pour les utilisateurs Firebase
        newUser.setNom("Utilisateur");
        newUser.setPrenom("Google");
        newUser.setTelephone("0000000000");
        newUser.setNomRue("Rue inconnue");
        newUser.setCodePostal("00000");
        newUser.setVille("Inconnue");
        newUser.setRole(RolesEnum.PROPRIO_LOCATAIRE); // Tu peux ajuster selon ton besoin

        Utilisateur utilisateur = saveUtilisateur(newUser);
        return utilisateurMapper.toDto(utilisateur);
    }

    @Override
    public UtilisateurDto registerUser(UtilisateurDto utilisateurDto) {
        // Hacher le mot de passe
        String encodedPassword = passwordEncoder.encode(utilisateurDto.getMotDePasse());
        utilisateurDto.setMotDePasse(encodedPassword);

        utilisateurDto.setRole(RolesEnum.PROPRIO_LOCATAIRE);
        // Générer et stocker un code de validation
        String validationCode = CodeGenerator.generateCode(6);
        utilisateurDto.setValidationCode(validationCode);

        // Marquer l'utilisateur comme non vérifié
        utilisateurDto.setEmailValidated(false);

        // Convertir le DTO en entité et sauvegarder
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDto);
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);

        // Envoyer le code de validation par email
        emailService.sendValidationEmail(utilisateurDto.getEmail(), validationCode);

        // Retourner le DTO de l'utilisateur sauvegardé
        return utilisateurMapper.toDto(savedUtilisateur);
    }

    @Override
    public boolean validateEmail(String email, String code) {
        // 1. Recherche de l'utilisateur par son email
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElse(null);

        // 2. Vérification si l'utilisateur existe et si les codes correspondent
        if (utilisateur != null && code.equals(utilisateur.getValidationCode())) {
            // 3. Mise à jour de l'état de validation et du code
            utilisateur.setEmailValidated(true);
            utilisateur.setValidationCode(null); // Optionnel, mais recommandé pour des raisons de sécurité

            // 4. Sauvegarde des changements dans la base de données
            utilisateurRepository.save(utilisateur);
            return true; // Succès
        }
        return false; // Échec
    }

    @Override
    public void resendValidationCode(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec cet email."));

        String newCode = CodeGenerator.generateCode(6);
        utilisateur.setValidationCode(newCode);
        utilisateurRepository.save(utilisateur);

        emailService.sendValidationEmail(email, newCode);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }
}
