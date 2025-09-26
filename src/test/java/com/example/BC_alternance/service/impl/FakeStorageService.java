package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.service.StorageService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Profile("test")
public class FakeStorageService implements StorageService {

    @Override
    public void init() {
        // rien à faire
    }

    @Override
    public String store(MultipartFile file) {
        // retourne une URL factice
        return "http://localhost/fake/" + file.getOriginalFilename();
    }

    @Override
    public Path load(String filename) {
        // retourne un fichier temporaire vide
        try {
            Path temp = Files.createTempFile("fake-", filename);
            temp.toFile().deleteOnExit();
            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
