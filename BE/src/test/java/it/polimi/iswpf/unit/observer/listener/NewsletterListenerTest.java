package it.polimi.iswpf.unit.observer.listener;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.model.entity.Evento;
import it.polimi.iswpf.model.entity.User;
import it.polimi.iswpf.observer.listener.NewsletterListener;
import it.polimi.iswpf.service.implementation.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class NewsletterListenerTest {

    @Mock
    EmailServiceImpl emailService;

    @InjectMocks
    NewsletterListener newsletterListener;

    @Test
    void updateThrowsCampiNonEsistenti() {

        User organizzatore = new UserBuilder()
                .nome("nome")
                .cognome("cognome")
                .build();

        List<User> turisti = new ArrayList<>();

        turisti.add(new UserBuilder()
                .email("fabriziofontana02@gmail.com")
                .build());

        Evento evento = new EventoBuilder()
                .titolo("titolo")
                .descrizione("descrizione")
                .dataInizio(LocalDateTime.now())
                .dataFine(null)
                .build();

        assertThrows(BadRequestException.class,
                () -> newsletterListener.update(organizzatore, turisti, evento));
    }

    @Test
    void updateThrowsCampoNonEsistente() {

        User organizzatore = new UserBuilder()
                .nome("nome")
                .cognome("cognome")
                .build();

        List<User> turisti = new ArrayList<>();

        turisti.add(new UserBuilder()
                .nome("Fabrizio")
                .email("")
                .build());

        Evento evento = new EventoBuilder()
                .titolo("titolo")
                .descrizione("descrizione")
                .dataInizio(LocalDateTime.now())
                .dataFine(LocalDateTime.now())
                .build();

        assertThrows(BadRequestException.class,
                () -> newsletterListener.update(organizzatore, turisti, evento));
    }

    @Test
    void updateSuccessful() {

        User organizzatore = new UserBuilder()
                .nome("nome")
                .cognome("cognome")
                .build();

        List<User> turisti = new ArrayList<>();

        turisti.add(new UserBuilder()
                .nome("Fabrizio")
                .email("fabriziofontana02@gmail.com")
                .build());

        Evento evento = new EventoBuilder()
                .titolo("titolo")
                .descrizione("descrizione")
                .dataInizio(LocalDateTime.now())
                .dataFine(LocalDateTime.now())
                .build();

        assertAll(() -> newsletterListener.update(organizzatore, turisti, evento));
    }
}