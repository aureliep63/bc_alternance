package com.example.BC_alternance.service;

import com.example.BC_alternance.dto.UtilisateurDto;
import com.example.BC_alternance.model.Utilisateur;

import java.util.List;

public interface UtilisateurService {

    List<UtilisateurDto> getAllUtilisateurs();

    UtilisateurDto getUtilisateurById(Long id);

    Utilisateur saveUtilisateur(UtilisateurDto utilisateurDto);

    void deleteUtilisateur(Long id);

    UtilisateurDto getUtilisateurByEmail(String email);

    UtilisateurDto findOrCreateFromFirebase(String email);

    UtilisateurDto registerUser(UtilisateurDto utilisateurDto);

    boolean validateEmail(String email, String code);

    void resendValidationCode(String email);

    boolean checkEmailExists(String email);
}
