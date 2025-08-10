package com.example.BC_alternance.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        // Lire la configuration Firebase à partir de la variable d'environnement
        String firebaseConfigJson = System.getenv("FIREBASE_CONFIG_JSON");

        // Vérifier si la variable d'environnement est définie et non vide
        if (firebaseConfigJson != null && !firebaseConfigJson.isEmpty()) {
            // Créer un InputStream à partir de la chaîne JSON
            InputStream serviceAccount = new ByteArrayInputStream(firebaseConfigJson.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Initialiser l'application Firebase si elle n'a pas déjà été initialisée
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } else {
            // Lancer une exception si la variable d'environnement n'est pas configurée,
            // ce qui empêchera l'application de démarrer.
            throw new IOException("La variable d'environnement 'FIREBASE_CONFIG_JSON' n'est pas configurée. L'application ne peut pas démarrer.");
        }
    }
}
