package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.RecensioneBuilder;
import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.InviaRecensioneRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.ForbiddenException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.Recensione;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.repository.RecensioneRepository;
import it.polimi.iswpf.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecensioneServiceImplTest {

    @Mock
    RecensioneRepository recensioneRepository;

    @Mock
    EventoRepository eventoRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RecensioneServiceImpl recensioneService;

    @Test
    void inviaRecensioneThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> recensioneService.inviaRecensione(null, 1L, 0L));
    }

    @Test
    void inviaRecensioneThrowsEventoNonTrovato() {

        when(eventoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> recensioneService.inviaRecensione(null, 1L, 2L));
    }

    @Test
    void inviaRecensioneThrowsTuristaNonTrovato() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(new Evento()));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> recensioneService.inviaRecensione(null, 1L, 2L));
    }

    @Test
    void inviaRecensioneThrowsPermessiNonAdatti() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(new Evento()));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(ForbiddenException.class,
                () -> recensioneService.inviaRecensione(null, 1L, 2L));
    }

    @Test
    void inviaRecensioneThrowsTuristaNonIscrittoAEvento() {

        Evento evento = new EventoBuilder()
                .eventoId(1L)
                .iscritti(List.of())
                .build();

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.TURISTA)
                .build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertThrows(ForbiddenException.class,
                () -> recensioneService.inviaRecensione(null, 1L, 2L));
    }

    @Test
    void inviaRecensioneThrowsMassimoUnaRecensione() {

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.TURISTA)
                .build();

        Recensione recensione = new RecensioneBuilder()
                .recensioneId(1L)
                .user(turista)
                .build();

        Evento evento = new EventoBuilder()
                .eventoId(1L)
                .iscritti(List.of(turista))
                .recensioni(List.of(recensione))
                .build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertThrows(BadRequestException.class,
                () -> recensioneService.inviaRecensione(null, 1L, 2L));
    }

    @Test
    void inviaRecensioneThrowsVotoNonValido() {

        InviaRecensioneRequest request = new InviaRecensioneRequest(0, "");

        Recensione recensione = new RecensioneBuilder()
                .voto(1)
                .testo("x")
                .user(new User())
                .build();

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.TURISTA)
                .build();

        Evento evento = new EventoBuilder()
                .eventoId(1L)
                .iscritti(List.of(turista))
                .recensioni(List.of(recensione))
                .build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertThrows(BadRequestException.class,
                () -> recensioneService.inviaRecensione(request, 1L, 2L));
    }

    @Test
    void inviaRecensioneSuccessful() {

        InviaRecensioneRequest request = new InviaRecensioneRequest(1, "ok");

        Recensione recensione = new RecensioneBuilder()
                .voto(1)
                .testo("x")
                .user(new User())
                .build();

        User turista = new UserBuilder()
                .userId(2L)
                .ruolo(Ruolo.TURISTA)
                .build();

        Evento evento = new EventoBuilder()
                .eventoId(1L)
                .iscritti(List.of(turista))
                .recensioni(List.of(recensione))
                .build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(userRepository.findByUserId(2L)).thenReturn(Optional.of(turista));

        assertAll(() -> recensioneService.inviaRecensione(request, 1L, 2L));
    }

    @Test
    void getByEventoThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> recensioneService.getByEvento(0L));
    }

    @Test
    void getByEventoThrowsEventoNonTrovato() {

        when(eventoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> recensioneService.getByEvento(1L));
    }

    @Test
    void getByEventoSuccessful() {

        Evento evento = new EventoBuilder()
                .eventoId(1L)
                .recensioni(List.of(new RecensioneBuilder()
                                .user(new User())
                                .voto(1)
                                .testo("")
                        .build()))
                .build();

        when(eventoRepository.findById(any())).thenReturn(Optional.of(evento));

        assertAll(() -> recensioneService.getByEvento(1L));
    }

    @Test
    void getRecensioneThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> recensioneService.getRecensione(0L, ""));
    }

    @Test
    void getRecensioneThrowsEventoNonTrovato() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> recensioneService.getRecensione(1L, ""));
    }

    @Test
    void getRecensioneThrowsUsernameNonValido() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(new Evento()));

        assertThrows(BadRequestException.class,
                () -> recensioneService.getRecensione(1L, ""));
    }

    @Test
    void getRecensioneThrowsUsernameNonRegistrato() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(new Evento()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> recensioneService.getRecensione(1L, "fonti"));
    }

    @Test
    void getRecensioneThrowsPermessiNonAdatti() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(new Evento()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(ForbiddenException.class,
                () -> recensioneService.getRecensione(1L, "fonti"));
    }

    @Test
    void getRecensioneThrowsRecensioneNonEsistente() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(new Evento()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));
        when(recensioneRepository.getRecensioneByEvento_EventoIdAndUser_Username(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> recensioneService.getRecensione(1L, "fonti"));
    }

    @Test
    void getRecensioneSuccessful() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(new Evento()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));
        when(recensioneRepository.getRecensioneByEvento_EventoIdAndUser_Username(any(), any()))
                .thenReturn(Optional.of(new Recensione()));

        assertAll(() -> recensioneService.getRecensione(1L, "fonti"));
    }
}