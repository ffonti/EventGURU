package it.polimi.iswpf.unit.builder;

import it.polimi.iswpf.builder.RecensioneBuilder;
import it.polimi.iswpf.model.entity.Evento;
import it.polimi.iswpf.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecensioneBuilderTest {

    @InjectMocks
    RecensioneBuilder recensioneBuilder;

    @Test
    void testAllMethods() {

        assertAll(() -> recensioneBuilder
                        .recensioneId(1L)
                        .testo("testo")
                        .voto(2)
                        .dataCreazione(LocalDateTime.now())
                        .user(new User())
                        .evento(new Evento())
                        .build());
    }
}