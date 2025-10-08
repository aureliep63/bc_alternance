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
import com.example.BC_alternance.service.SendGridEmailService;
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
    private final PasswordEncoder passwordEncoder;
    private SendGridEmailService sendGridEmailService;


    public UtilisateurServiceImpl( SendGridEmailService sendGridEmailService,
                                   PasswordEncoder passwordEncoder,UtilisateurRepository utilisateurRepository, BorneRepository borneRepository,ReservationRepository reservationRepository,
                                  UtilisateurMapper utilisateurMapper){
        this.utilisateurRepository = utilisateurRepository;
        this.borneRepository = borneRepository;
        this.reservationRepository = reservationRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.passwordEncoder = passwordEncoder;
        this.sendGridEmailService = sendGridEmailService;
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

        // Si utilisateur n'existe pas, un fake
        UtilisateurDto newUser = new UtilisateurDto();
        newUser.setEmail(email);
        newUser.setNom("Utilisateur");
        newUser.setPrenom("Google");
        newUser.setMotDePasse("F@kePass123");
        newUser.setTelephone("0612345678");
        newUser.setNomRue("Rue inconnue");
        newUser.setCodePostal("00000");
        newUser.setVille("Inconnue");
        newUser.setRole(RolesEnum.PROPRIO_LOCATAIRE);

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
        utilisateur.setTentativesRestantes(3);
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);

        // Envoyer le code de validation par email
       // emailService.sendValidationEmail(utilisateurDto.getEmail(), validationCode);
        sendGridEmailService.sendValidationEmail(utilisateurDto.getEmail(), validationCode);


        // Retourner le DTO de l'utilisateur sauvegardé
        return utilisateurMapper.toDto(savedUtilisateur);
    }

    @Override
    public boolean validateEmail(String email, String code) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElse(null);
        if (utilisateur == null) return false;

        if (utilisateur.isEmailValidated()) return true;

        if (code.equals(utilisateur.getValidationCode())) {
            utilisateur.setEmailValidated(true);
            utilisateur.setValidationCode(null);
            utilisateur.setTentativesRestantes(3);
            utilisateurRepository.save(utilisateur);
            return true;
        }

        // Mauvais code
        int restantes = utilisateur.getTentativesRestantes() - 1;
        utilisateur.setTentativesRestantes(restantes);

        if (restantes <= 0) {
            String nouveauCode = CodeGenerator.generateCode(6);
            utilisateur.setValidationCode(nouveauCode);
            utilisateur.setTentativesRestantes(3);
            utilisateurRepository.save(utilisateur);
            sendGridEmailService.sendValidationEmail(email, nouveauCode);
            throw new IllegalArgumentException("Code incorrect. Un nouveau code a été envoyé.");
        }

        utilisateurRepository.save(utilisateur);
        throw new IllegalArgumentException("Code incorrect. Il vous reste " + restantes + " tentative(s).");
    }


    @Override
    public void resendValidationCode(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec cet email."));

        String newCode = CodeGenerator.generateCode(6);
        utilisateur.setValidationCode(newCode);
        utilisateurRepository.save(utilisateur);

       // emailService.sendValidationEmail(email, newCode);
        sendGridEmailService.sendValidationEmail(email, newCode);

    }

    @Override
    public boolean checkEmailExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }


}
