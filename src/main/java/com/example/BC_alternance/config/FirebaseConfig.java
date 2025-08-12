package com.example.BC_alternance.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_CONFIG_PATH:}")
    private String firebaseConfigPath;

    @PostConstruct
    public void init() throws IOException {
        String firebaseConfigJson = System.getenv("FIREBASE_CONFIG_JSON");

        InputStream serviceAccount;

        if (firebaseConfigJson != null && !firebaseConfigJson.isEmpty()) {
            // Mode production (Render)
            serviceAccount = new ByteArrayInputStream(firebaseConfigJson.getBytes(StandardCharsets.UTF_8));
        } else if (firebaseConfigPath != null && !firebaseConfigPath.isEmpty()) {
            // Mode local : lecture depuis un fichier
            serviceAccount = new ClassPathResource(firebaseConfigPath.replace("classpath:", "")).getInputStream();
        } else {
            throw new IOException("Aucune configuration Firebase trouvée (ni FIREBASE_CONFIG_JSON, ni FIREBASE_CONFIG_PATH).");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}

