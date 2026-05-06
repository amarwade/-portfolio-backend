package com.portfolio.backend.service;

import com.portfolio.backend.model.ContactMessage;
import com.portfolio.backend.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private JavaMailSender mailSender;

    @Test
    void shouldSaveContactMessage() {
        ContactService contactService = new ContactService(
                contactRepository,
                mailSender,
                false,
                "",
                "noreply@portfolio.local"
        );

        ContactMessage input = new ContactMessage(
                null,
                "Alice",
                "alice@example.com",
                "Demande de stage",
                "Bonjour, je vous contacte pour un stage.",
                null
        );

        ContactMessage saved = new ContactMessage(
                10L,
                "Alice",
                "alice@example.com",
                "Demande de stage",
                "Bonjour, je vous contacte pour un stage.",
                null
        );

        when(contactRepository.save(input)).thenReturn(saved);

        ContactMessage result = contactService.save(input);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        verify(contactRepository).save(input);
    }
}
