package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path root;

    public StorageServiceImpl() {
        // Chemin unique, à la racine du projet
        this.root = Paths.get("upload").toAbsolutePath().normalize();
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier upload", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) throw new RuntimeException("Fichier vide");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path target = root.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur stockage fichier", e);
        }
        return filename;
    }

    @Override
    public Path load(String filename) {
        Path filePath = root.resolve(filename);
        // Vérifie si le fichier existe dans le filesystem
        if (Files.exists(filePath)) {
            return filePath;
        }

        // Si on est dans un JAR, essaie de charger depuis les resources
        try (InputStream is = getClass().getResourceAsStream("/upload/" + filename)) {
            if (is == null) throw new FileNotFoundException(filename);
            Path temp = Files.createTempFile("upload-", filename);
            Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);
            temp.toFile().deleteOnExit(); // nettoie le fichier temporaire à la sortie
            return temp;
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger le fichier", e);
        }
    }
}
