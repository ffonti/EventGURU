package it.polimi.iswpf.unit.service;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.LuogoBuilder;
import it.polimi.iswpf.builder.RecensioneBuilder;
import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.DatiCirconferenza;
import it.polimi.iswpf.dto.request.PuntoPoligono;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.service.implementation.LuogoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LuogoServiceImplTest {

    @Mock
    EventoRepository eventoRepository;

    @InjectMocks
    LuogoServiceImpl luogoService;

    @Test
    void getAllMarkerCoordinatesSuccessful() {

        when(eventoRepository.findAll()).thenReturn(List.of(new EventoBuilder()
                .dataInizio(LocalDateTime.now().plusHours(1))
                .iscritti(List.of(new UserBuilder()
                    .username("fonti")
                    .build()))
                .recensioni(List.of(new RecensioneBuilder()
                                .user(new UserBuilder().username("fonti").build())
                                .testo("Testo")
                                .voto(1)
                        .build()))
                .luogo(new LuogoBuilder()
                        .lat("2")
                        .lng("2")
                        .nome("nome luogo")
                        .build())
                .organizzatore(new UserBuilder().username("orga").build())
                .build()));

        assertAll(() -> luogoService.getAllMarkerCoordinates());
    }

    @Test
    void getAllMarkerCoordinatesByOrganizzatoreThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> luogoService.getAllMarkerCoordinatesByOrganizzatore(0L));
    }

    @Test
    void getAllMarkerCoordinatesByOrganizzatoreSuccessful() {

        when(eventoRepository.findAllByOrganizzatoreUserId(1L)).thenReturn(List.of(new EventoBuilder()
                .dataInizio(LocalDateTime.now().minusHours(2))
                .dataFine(LocalDateTime.now().minusHours(1))
                .iscritti(List.of(new UserBuilder()
                        .username("fonti")
                        .build()))
                .recensioni(List.of(new RecensioneBuilder()
                        .user(new UserBuilder().username("fonti").build())
                        .testo("Testo")
                        .voto(1)
                        .build()))
                .luogo(new LuogoBuilder()
                        .lat("2")
                        .lng("2")
                        .nome("nome luogo")
                        .build())
                .organizzatore(new UserBuilder().username("org").build())
                .build()));

        assertAll(() -> luogoService.getAllMarkerCoordinatesByOrganizzatore(1L));
    }

    @Test
    void statoEventoPassato() {

        when(eventoRepository.findAllByOrganizzatoreUserId(1L)).thenReturn(List.of(new EventoBuilder()
                .dataInizio(LocalDateTime.now().minusHours(1))
                .dataFine(LocalDateTime.now().plusHours(1))
                .iscritti(List.of(new UserBuilder()
                        .username("fonti")
                        .build()))
                .recensioni(List.of(new RecensioneBuilder()
                        .user(new UserBuilder().username("fonti").build())
                        .testo("Testo")
                        .voto(1)
                        .build()))
                .luogo(new LuogoBuilder()
                        .lat("2")
                        .lng("2")
                        .nome("nome luogo")
                        .build())
                .organizzatore(new UserBuilder().username("org").build())
                .build()));

        assertAll(() -> luogoService.getAllMarkerCoordinatesByOrganizzatore(1L));
    }

    @Test
    void coordinateDentroPoligonoSuccessful() {

        List<PuntoPoligono> request = List.of(
                new PuntoPoligono("41.88175157054525", "12.581780851355065"),
                new PuntoPoligono("42.029673096144364", "12.4484281845193"),
                new PuntoPoligono("41.84088522282816", "12.392062624310386")
        );

        when(eventoRepository.findAll()).thenReturn(List.of(new EventoBuilder()
                    .luogo(new LuogoBuilder()
                            .lat("41.91895802952411")
                            .lng("12.454955445376672")
                            .build())
                    .dataInizio(LocalDateTime.now().plusHours(1))
                    .iscritti(List.of(new UserBuilder()
                            .username("fonti")
                            .build()))
                    .recensioni(List.of(new RecensioneBuilder()
                            .user(new UserBuilder().username("fonti").build())
                            .testo("Testo")
                            .voto(1)
                            .build()))
                    .organizzatore(new UserBuilder().username("org").build())
                    .build()));

        assertAll(() -> luogoService.coordinateDentroPoligono(request));
    }

    @Test
    void coordinateDentroPoligonoByOrganizzatoreThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> luogoService.coordinateDentroPoligonoByOrganizzatore(null, 0L));
    }

    @Test
    void coordinateDentroPoligonoByOrganizzatoreSuccessful() {

        List<PuntoPoligono> request = List.of(
                new PuntoPoligono("41.88175157054525", "12.581780851355065"),
                new PuntoPoligono("42.029673096144364", "12.4484281845193"),
                new PuntoPoligono("41.84088522282816", "12.392062624310386")
        );

        when(eventoRepository.findAllByOrganizzatoreUserId(1L)).thenReturn(List.of(new EventoBuilder()
                .luogo(new LuogoBuilder()
                        .lat("41.91895802952411")
                        .lng("12.454955445376672")
                        .build())
                .dataInizio(LocalDateTime.now().plusHours(1))
                .iscritti(List.of(new UserBuilder()
                        .username("fonti")
                        .build()))
                .recensioni(List.of(new RecensioneBuilder()
                        .user(new UserBuilder().username("fonti").build())
                        .testo("Testo")
                        .voto(1)
                        .build()))
                .organizzatore(new UserBuilder().username("org").build())
                .build()));

        assertAll(() -> luogoService.coordinateDentroPoligonoByOrganizzatore(request, 1L));
    }

    @Test
    void coordinateDentroCirconferenzaSuccessful() {

        DatiCirconferenza request = new DatiCirconferenza(
                "41.915494890429066",
                "12.456965769797526",
                "4473.508082462737"
        );

        when(eventoRepository.findAll()).thenReturn(List.of(new EventoBuilder()
                .luogo(new LuogoBuilder()
                        .lat("41.91895802952411")
                        .lng("12.454955445376672")
                        .build())
                .dataInizio(LocalDateTime.now().plusHours(1))
                .iscritti(List.of(new UserBuilder()
                        .username("fonti")
                        .build()))
                .recensioni(List.of(new RecensioneBuilder()
                        .user(new UserBuilder().username("fonti").build())
                        .testo("Testo")
                        .voto(1)
                        .build()))
                .organizzatore(new UserBuilder().username("org").build())
                .build()));

        assertAll(() -> luogoService.coordinateDentroCirconferenza(request));
    }

    @Test
    void coordinateDentroCirconferenzaByOrganizzatoreThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> luogoService.coordinateDentroCirconferenzaByOrganizzatore(null, 0L));
    }

    @Test
    void coordinateDentroCirconferenzaByOrganizzatoreSuccessful() {

        DatiCirconferenza request = new DatiCirconferenza(
                "41.915494890429066",
                "12.456965769797526",
                "4473.508082462737"
        );

        when(eventoRepository.findAllByOrganizzatoreUserId(1L)).thenReturn(List.of(new EventoBuilder()
                .luogo(new LuogoBuilder()
                        .lat("41.91895802952411")
                        .lng("12.454955445376672")
                        .build())
                .dataInizio(LocalDateTime.now().plusHours(1))
                .iscritti(List.of(new UserBuilder()
                        .username("fonti")
                        .build()))
                .recensioni(List.of(new RecensioneBuilder()
                        .user(new UserBuilder().username("fonti").build())
                        .testo("Testo")
                        .voto(1)
                        .build()))
                .organizzatore(new UserBuilder().username("org").build())
                .build()));

        assertAll(() -> luogoService.coordinateDentroCirconferenzaByOrganizzatore(request, 1L));
    }
}