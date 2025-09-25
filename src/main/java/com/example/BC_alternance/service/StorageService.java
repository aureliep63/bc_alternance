package com.example.BC_alternance.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    /**
     * Initialise le service de stockage, par exemple en créant le répertoire de base.
     */
    void init();

    /**
     * Sauvegarde un fichier MultipartFile et retourne son nom généré.
     * @param file le fichier à sauvegarder.
     * @return le nom du fichier stocké.
     */
    String store(MultipartFile file);

    /**
     * Charge un fichier par son nom.
     * @param filename le nom du fichier.
     * @return le chemin d'accès au fichier.
     */
    Path load(String filename);

}