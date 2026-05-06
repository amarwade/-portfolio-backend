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

@Service
public class ContactService {
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final JavaMailSender mailSender;
    private final boolean mailEnabled;
    private final String mailTo;
    private final String mailFrom;

    public ContactService(
            ContactRepository contactRepository,
            JavaMailSender mailSender,
            @Value("${app.mail.enabled:false}") boolean mailEnabled,
            @Value("${app.mail.to:}") String mailTo,
            @Value("${app.mail.from:noreply@portfolio.local}") String mailFrom
    ) {
        this.contactRepository = contactRepository;
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
        this.mailTo = mailTo;
        this.mailFrom = mailFrom;
    }

    public ContactMessage save(ContactMessage contactMessage) {
        ContactMessage saved = contactRepository.save(contactMessage);
        sendNotificationEmail(saved);
        return saved;
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
