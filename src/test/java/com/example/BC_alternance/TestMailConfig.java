package com.example.BC_alternance;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestMailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // Retourne un mock pour éviter les erreurs dans les tests
        return Mockito.mock(JavaMailSender.class);
    }
}