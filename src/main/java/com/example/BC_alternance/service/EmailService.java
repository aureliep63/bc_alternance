package com.example.BC_alternance.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendValidationEmail(String toEmail, String validationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("aurelie.pedro.r@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Validez votre inscription");
        message.setText("Merci de vous être inscrit. Votre code de validation est : " + validationCode + ". Ce code est valide pour une durée limitée.");
        mailSender.send(message);
    }
}