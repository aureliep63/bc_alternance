package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Profile("local")
public class LocalStorageService implements StorageService {

    private final Path root;

    public LocalStorageService(@Value("${upload.path}") String uploadPath) {
        this.root = Paths.get(uploadPath).toAbsolutePath().normalize();
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

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        int lastDot = originalFilename.lastIndexOf('.');
        if (lastDot > 0) {
            extension = originalFilename.substring(lastDot);
        }

        // Génére un nom unique avec UUID
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        try {
            Path target = root.resolve(uniqueFilename);

            try (java.io.InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }

            // Retourne le nom de fichier unique
            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Erreur stockage fichier", e);
        }
    }

    @Override
    public Path load(String filename) {
        return root.resolve(filename);
    }
}
