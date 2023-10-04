package it.polimi.iswpf.unit.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.InviaEmailRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.InternalServerErrorException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model.EventType;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service.implementation.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    Configuration config;

    @Mock
    MimeMessage mimeMessage;

    @Mock
    Template template;

    @Mock
    JavaMailSender emailSender;

    @InjectMocks
    EmailServiceImpl emailService;

    @Test
    void inviaEmailThrowsErroreInvioEmail() {

        InviaEmailRequest request = new InviaEmailRequest(
                "fabriziofontana02@gmail.com",
                "Oggettto della mail",
                new HashMap<>(),
                EventType.RECOVERY_PASSWORD
        );

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);


        try {
            when(config.getTemplate(any())).thenThrow(IOException.class);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        assertThrows(InternalServerErrorException.class,
                () -> emailService.inviaEmail(request));
    }

    @Test
    void inviaEmailSuccessful() {

        InviaEmailRequest request = new InviaEmailRequest(
                "fabriziofontana02@gmail.com",
                "Oggettto della mail",
                new HashMap<>(),
                EventType.RECOVERY_PASSWORD
        );

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        try {
            when(config.getTemplate(any())).thenReturn(template);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        assertAll(() -> emailService.inviaEmail(request));
    }

    @Test
    void recuperaPasswordThrowsEmailNonValida() {

        assertThrows(BadRequestException.class,
                () -> emailService.recuperaPassword(""));
    }

    @Test
    void recuperaPasswordThrowsEmailNonPresente() {

        when(userRepository.findFirstByEmail(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> emailService.recuperaPassword("fabriziofontana02@gmail.com"));
    }

    @Test
    void recuperaPasswordSuccessful() {

        User user = new UserBuilder()
                .userId(1L)
                .build();

        when(userRepository.findFirstByEmail(any())).thenReturn(Optional.of(user));

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        try {
            when(config.getTemplate(any())).thenReturn(template);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        assertAll(() -> emailService.recuperaPassword("fabriziofontana02@gmail.com"));
    }
}