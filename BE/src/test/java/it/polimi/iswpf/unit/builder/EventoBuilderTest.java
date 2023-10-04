package it.polimi.iswpf.unit.builder;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.model.Luogo;
import it.polimi.iswpf.model.Recensione;
import it.polimi.iswpf.model.Stato;
import it.polimi.iswpf.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventoBuilderTest {

    @InjectMocks
    EventoBuilder eventoBuilder;

    @Test
    void testAllMethods() {

        assertAll(() -> eventoBuilder
                .eventoId(1L)
                .titolo("titolo")
                .descrizione("descrizione")
                .dataCreazione(LocalDateTime.now())
                .dataInizio(LocalDateTime.now().plusHours(1))
                .dataFine(LocalDateTime.now().plusHours(2))
                .stato(Stato.FUTURO)
                .iscritti(List.of(new User()))
                .recensioni(List.of(new Recensione()))
                .organizzatore(new User())
                .luogo(new Luogo())
                .build());
    }
}