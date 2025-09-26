package com.example.BC_alternance.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    Path load(String filename);
}
