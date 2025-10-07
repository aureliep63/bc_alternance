package com.example.BC_alternance.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.sender.email}")
    private String senderEmail;

    public void sendValidationEmail(String toEmail, String validationCode) {
        if (sendGridApiKey == null || sendGridApiKey.equals("dummy")) {
            System.out.println("Envoi d'e-mail désactivé (profil test ou clé manquante)");
            return;
        }

        Email from = new Email(senderEmail);
        Email to = new Email(toEmail);
        String contentText = "Merci de vous être inscrit. Votre code de validation est : " + validationCode + ". Ce code est valide pour une durée limitée.";
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, "Validez votre inscription", to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("SendGrid response: " + response.getStatusCode());
        } catch (IOException ex) {
            System.err.println("Erreur SendGrid : " + ex.getMessage());
        }
    }
}
