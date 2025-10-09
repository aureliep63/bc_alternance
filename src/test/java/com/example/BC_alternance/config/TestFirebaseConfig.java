
package com.example.BC_alternance.config;

import com.google.firebase.FirebaseApp;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * Configuration de test pour simuler FirebaseApp.
 */
@Configuration
@Profile("test") // S'assure que cette config est chargée uniquement dans le profil 'test'
public class TestFirebaseConfig {

    @Bean
    @Primary
    public FirebaseApp firebaseApp() {

        FirebaseApp mockedApp = Mockito.mock(FirebaseApp.class);

        // Optionnel : Simuler la récupération de l'instance par défaut
        // if (FirebaseApp.getApps().isEmpty()) {
        //     Mockito.when(mockedApp.getName()).thenReturn("[DEFAULT]");
        // }

        return mockedApp;
    }
}