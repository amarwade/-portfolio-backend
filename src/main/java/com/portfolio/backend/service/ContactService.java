package com.portfolio.backend.service;

import com.portfolio.backend.model.ContactMessage;
import com.portfolio.backend.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;

@Service
public class ContactService {
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final JavaMailSender mailSender;
    private final boolean mailEnabled;
    private final String mailTo;
    private final String mailFrom;
    private final String discordWebhookUrl;

    public ContactService(
            ContactRepository contactRepository,
            JavaMailSender mailSender,
            @Value("${app.mail.enabled:false}") boolean mailEnabled,
            @Value("${app.mail.to:}") String mailTo,
            @Value("${app.mail.from:noreply@portfolio.local}") String mailFrom,
            @Value("${app.discord.webhook:}") String discordWebhookUrl
    ) {
        this.contactRepository = contactRepository;
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
        this.mailTo = mailTo;
        this.mailFrom = mailFrom;
        this.discordWebhookUrl = discordWebhookUrl;
    }

    public ContactMessage save(ContactMessage contactMessage) {
        ContactMessage saved = contactRepository.save(contactMessage);
        sendNotificationEmail(saved);
        sendDiscordNotification(saved);
        return saved;
    }

    private void sendDiscordNotification(ContactMessage message) {
        if (discordWebhookUrl == null || discordWebhookUrl.isBlank()) {
            log.debug("Discord webhook non configure, notification Discord ignoree");
            return;
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            
            String jsonPayload = String.format(
                "{\"embeds\":[{\"title\":\"📧 Nouveau message de contact\",\"color\":3447003,\"fields\":[{\"name\":\"Nom\",\"value\":\"%s\",\"inline\":true},{\"name\":\"Email\",\"value\":\"%s\",\"inline\":true},{\"name\":\"Sujet\",\"value\":\"%s\",\"inline\":false},{\"name\":\"Message\",\"value\":\"%s\",\"inline\":false}],\"footer\":{\"text\":\"Portfolio Backend\"},\"timestamp\":\"%s\"}]}",
                escapeJson(message.getName()),
                escapeJson(message.getEmail()),
                escapeJson(message.getSubject()),
                escapeJson(message.getMessage().length() > 1000 ? message.getMessage().substring(0, 1000) + "..." : message.getMessage()),
                java.time.Instant.now().toString()
            );

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(discordWebhookUrl))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.statusCode() == 200) {
                log.info("Notification Discord envoyee pour le contact de {}", message.getEmail());
            } else if (response.statusCode() == 429) {
                log.debug("Rate limit Discord atteint pour {}, notification ignoree", message.getEmail());
            } else {
                log.warn("Echec envoi Discord: HTTP {}", response.statusCode());
            }
        } catch (Exception ex) {
            log.warn("Message enregistre mais notification Discord impossible pour le contact {}", message.getEmail(), ex);
        }
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private void sendNotificationEmail(ContactMessage message) {
        if (!mailEnabled || mailTo.isBlank()) {
            return;
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(mailFrom);
        mail.setTo(mailTo);
        mail.setReplyTo(message.getEmail());
        mail.setSubject("[Portfolio] Nouveau message de " + message.getName());
        mail.setText(buildEmailBody(message));

        try {
            mailSender.send(mail);
        } catch (MailException ex) {
            log.warn("Message enregistre mais envoi email impossible pour le contact {}", message.getEmail(), ex);
        }
    }

    private String buildEmailBody(ContactMessage message) {
        return "Nouveau message de contact recu:\n\n"
                + "Nom: " + message.getName() + "\n"
                + "Email: " + message.getEmail() + "\n"
                + "Sujet: " + message.getSubject() + "\n\n"
                + "Message:\n"
                + message.getMessage() + "\n";
    }
}
