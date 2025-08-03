package com.example.BC_alternance.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendValidationEmail(String toEmail, String validationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("aurelie.pedro.r@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Validez votre inscription");
        message.setText("Merci de vous être inscrit. Veuillez cliquer sur ce lien pour valider votre compte : " + validationLink);
        mailSender.send(message);
    }
}