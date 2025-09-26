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
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path target = root.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return filename; // en local on stocke juste le nom
        } catch (IOException e) {
            throw new RuntimeException("Erreur stockage fichier", e);
        }
    }

    @Override
    public Path load(String filename) {
        return root.resolve(filename);
    }
}
