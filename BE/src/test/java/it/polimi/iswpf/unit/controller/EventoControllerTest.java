package it.polimi.iswpf.unit.controller;

import it.polimi.iswpf.controller.EventoController;
import it.polimi.iswpf.dto.request.AdminCreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.CreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.IscrizioneEventoRequest;
import it.polimi.iswpf.service.implementation.EventoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventoControllerTest {

    @Mock
    EventoServiceImpl eventoService;

    @InjectMocks
    EventoController eventoController;

    @Test
    void creaEvento() {

        CreaModificaEventoRequest request =
                new CreaModificaEventoRequest("", "", LocalDateTime.now(), LocalDateTime.now(), "", "" , "");

        assertAll(() -> eventoController.creaEvento(request, "1"));
    }

    @Test
    void getEventiByOrganizzatore() {

        assertAll(() -> eventoController.getEventiByOrganizzatore("1"));
    }

    @Test
    void eliminaEvento() {

        assertAll(() -> eventoController.eliminaEvento("1"));
    }

    @Test
    void getEventoById() {

        assertAll(() -> eventoController.getEventoById("1"));
    }

    @Test
    void modificaEvento() {

        CreaModificaEventoRequest request =
                new CreaModificaEventoRequest("", "", LocalDateTime.now(), LocalDateTime.now(), "", "" , "");

        assertAll(() -> eventoController.modificaEvento(request, "1"));
    }

    @Test
    void iscrizioneEvento() {

        IscrizioneEventoRequest request = new IscrizioneEventoRequest(1L, 1L);

        assertAll(() -> eventoController.iscrizioneEvento(request));
    }

    @Test
    void getAllEventi() {

        assertAll(() -> eventoController.getAllEventi());
    }

    @Test
    void adminCreaEvento() {

        AdminCreaModificaEventoRequest request =
                new AdminCreaModificaEventoRequest("", "", LocalDateTime.now(), LocalDateTime.now(), "", "" , "", "");

        assertAll(() -> eventoController.adminCreaEvento(request));
    }

    @Test
    void adminModificaEvento() {

        AdminCreaModificaEventoRequest request =
                new AdminCreaModificaEventoRequest("", "", LocalDateTime.now(), LocalDateTime.now(), "", "" , "", "");

        assertAll(() -> eventoController.adminModificaEvento(request, "1"));
    }

    @Test
    void annullaIscrizione() {

        IscrizioneEventoRequest request =
                new IscrizioneEventoRequest(1L, 1L);

        assertAll(() -> eventoController.annullaIscrizione(request));
    }

    @Test
    void getEventiByTurista() {

        assertAll(() -> eventoController.getEventiByTurista("username"));
    }

    @Test
    void rimuoviTuristaDaEvento() {

        assertAll(() -> eventoController.rimuoviTuristaDaEvento("username", "1"));
    }
}