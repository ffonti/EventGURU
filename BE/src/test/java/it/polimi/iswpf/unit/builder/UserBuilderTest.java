package it.polimi.iswpf.unit.builder;

import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.Recensione;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserBuilderTest {

    @InjectMocks
    UserBuilder userBuilder;

    @Test
    void testAllMethods() {

        assertAll(() -> userBuilder
                .userId(1L)
                .nome("nome")
                .cognome("cognome")
                .email("email")
                .username("username")
                .password("password")
                .ruolo(Ruolo.TURISTA)
                .iscrittoNewsletter(false)
                .dataCreazione(LocalDateTime.now())
                .eventi(List.of(new Evento()))
                .recensioni(List.of(new Recensione()))
                .seguiti(List.of(new User()))
                .iscrizioni(List.of(new Evento()))
                .build());
    }
}