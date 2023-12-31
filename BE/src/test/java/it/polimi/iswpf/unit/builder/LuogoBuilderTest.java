package it.polimi.iswpf.unit.builder;

import it.polimi.iswpf.builder.LuogoBuilder;
import it.polimi.iswpf.model.entity.Evento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LuogoBuilderTest {

    @InjectMocks
    LuogoBuilder luogoBuilder;

    @Test
    void testaAllMethods() {

        assertAll(() -> luogoBuilder
                .luogoId(1L)
                .nome("nome luogo")
                .lat(1F)
                .lng(1F)
                .eventi(List.of(new Evento()))
                .build());
    }
}