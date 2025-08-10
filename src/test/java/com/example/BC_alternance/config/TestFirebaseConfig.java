package com.example.BC_alternance.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class TestFirebaseConfig {

    @Bean
    @Primary
    public FirebaseApp firebaseApp() throws IOException {
        // Crée une fausse configuration de Firebase pour les tests sans lire de fichier JSON
        FirebaseOptions options = FirebaseOptions.builder()
                // La meilleure solution est de définir les identifiants à null pour éviter
                // de lire un fichier ou de simuler des classes complexes.
                .setCredentials(null)
                .setDatabaseUrl("https://test-database.firebaseio.com")
                .build();

        // Initialise l'application Firebase avec les options de test
        return FirebaseApp.initializeApp(options, "test-app");
    }
}