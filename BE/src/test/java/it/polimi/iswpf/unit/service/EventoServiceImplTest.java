package it.polimi.iswpf.unit.service;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.LuogoBuilder;
import it.polimi.iswpf.builder.RecensioneBuilder;
import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.AdminCreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.CreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.IscrizioneEventoRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.ForbiddenException;
import it.polimi.iswpf.exception.InternalServerErrorException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model.*;
import it.polimi.iswpf.observer.listener.EventListener;
import it.polimi.iswpf.observer.listener.FollowersListener;
import it.polimi.iswpf.observer.listener.NewsletterListener;
import it.polimi.iswpf.observer.publisher.EventManager;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.repository.LuogoRepository;
import it.polimi.iswpf.repository.UserRepository;
import it.polimi.iswpf.service.implementation.EventoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventoServiceImplTest {

    @Mock
    FollowersListener followersListener;

    @Mock
    NewsletterListener newsletterListener;

    @Mock
    Map<EventType, EventListener> listeners;

    @Mock
    EventoRepository eventoRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    LuogoRepository luogoRepository;

    @Mock
    EventManager eventManager;

    @InjectMocks
    EventoServiceImpl eventoService;

    @Test
    void creaEventoThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        "1",
                        "1",
                        "nome luogo"
                ), 0L));
    }

    @Test
    void creaEventoThrowsOrganizzatoreNonTrovato() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        "1",
                        "1",
                        "nome luogo"
                ), 1L));
    }

    @Test
    void creaEventoThrowsPermessiNonAdatti() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        assertThrows(ForbiddenException.class,
                () -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        "1",
                        "1",
                        "nome luogo"
                ), 1L));
    }

    @Test
    void creaEventoThrowsDataInizioSuccessivaADataFine() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(BadRequestException.class,
                () -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now(),
                        "1",
                        "1",
                        "nome luogo"
                ), 1L));
    }

    @Test
    void creaEventoThrowsDataInizioUgualeADataFine() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(BadRequestException.class,
                () -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "1",
                        "1",
                        "nome luogo"
                ), 1L));
    }

    @Test
    void creaEventoThrowsDataInizioPrecedenteAdOra() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(BadRequestException.class,
                () -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now().minusHours(1),
                        LocalDateTime.now(),
                        "1",
                        "1",
                        "nome luogo"
                ), 1L));
    }

    @Test
    void creaEventoThrowsDataInizioUgualeAdOra() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(BadRequestException.class,
                () -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        "1",
                        "1",
                        "nome luogo"
                ), 1L));
    }

    @Test
    void creaEventoThrowsCampiNonCompilati() {

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(BadRequestException.class,
                () -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        "1",
                        "1",
                        ""
                ), 1L));
    }

    @Test
    void creaEventoSuccessful() {

        ReflectionTestUtils.setField(EventManager.getInstance(), "instance", eventManager);

        User organizzatore = new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build();

        User follower = new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build();

        List<User> followers = new ArrayList<>();

        List<User> iscrittiNewsletter = new ArrayList<>();

        followers.add(follower);
        iscrittiNewsletter.add(follower);
        iscrittiNewsletter.add(new User());

        when(userRepository.findByUserId(1L)).thenReturn(Optional.of(organizzatore));

        when(userRepository.findAllBySeguitiAndRuolo(organizzatore, Ruolo.TURISTA))
                .thenReturn(followers);

        when(userRepository.findAllByIscrittoNewsletterIsTrueAndRuolo(Ruolo.TURISTA))
                .thenReturn(iscrittiNewsletter);

        doNothing().when(EventManager.getInstance()).notify(any(), any(), any(), any());

        assertAll(() -> eventoService.creaEvento(new CreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo"), 1L));
    }

    @Test
    void getAllEventiThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventoService.getAllEventi(0L));
    }

    @Test
    void getAllEventiThrowsOrganizzatoreNonTrovato() {

        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.getAllEventi(1L));
    }

    @Test
    void getAllEventiThrowsNoEventi() {

        User organizzatore = new UserBuilder()
                .build();

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(organizzatore));

        when(eventoRepository.findAllByOrganizzatore(organizzatore))
                .thenReturn(Optional.empty());

        assertAll(() -> eventoService.getAllEventi(1L));
    }

    @Test
    void getAllEventiSuccessful() {

        User organizzatore = new UserBuilder()
                .build();

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(organizzatore));

        when(eventoRepository.findAllByOrganizzatore(organizzatore))
                .thenReturn(Optional.of(List.of(new EventoBuilder()
                                .eventoId(1L)
                                .titolo("titolo")
                                .descrizione("descrizione")
                                .dataInizio(LocalDateTime.now().plusHours(1))
                                .dataFine(LocalDateTime.now().plusHours(2))
                                .dataCreazione(LocalDateTime.now())
                                .luogo(new LuogoBuilder()
                                        .lat("1")
                                        .lng("1")
                                        .nome("nome luogo")
                                        .build())
                                .organizzatore(organizzatore)
                                .iscritti(List.of(new UserBuilder()
                                        .username("username")
                                        .build()))
                                .recensioni(List.of(new RecensioneBuilder()
                                        .user(new UserBuilder().username("username").build())
                                        .voto(1)
                                        .testo("testo")
                                        .build()))
                        .build())));

        assertAll(() -> eventoService.getAllEventi(1L));
    }

    @Test
    void eliminaEventoThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventoService.eliminaEvento(0L));
    }

    @Test
    void eliminaEventoThrowsEventoNonTrovato() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.eliminaEvento(1L));
    }

    @Test
    void eliminaEventoThrowsErroreEliminazione() {

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        assertThrows(InternalServerErrorException.class,
                () -> eventoService.eliminaEvento(1L));
    }

    @Test
    void getEventoByIdThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventoService.getEventoById(0L));
    }

    @Test
    void getEventoByIdThrowsEventoNonTrovato() {

        when(eventoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.getEventoById(1L));
    }

    @Test
    void getEventoByIdSuccessful() {

        User organizzatore = new UserBuilder()
                .build();

        when(eventoRepository.findById(any()))
                .thenReturn(Optional.of(new EventoBuilder()
                        .eventoId(1L)
                        .titolo("titolo")
                        .descrizione("descrizione")
                        .dataInizio(LocalDateTime.now().plusHours(1))
                        .dataFine(LocalDateTime.now().plusHours(2))
                        .dataCreazione(LocalDateTime.now())
                        .luogo(new LuogoBuilder()
                                .lat("1")
                                .lng("1")
                                .nome("nome luogo")
                                .build())
                        .organizzatore(organizzatore)
                        .iscritti(List.of(new UserBuilder()
                                .username("username")
                                .build()))
                        .recensioni(List.of(new RecensioneBuilder()
                                .user(new UserBuilder().username("username").build())
                                .voto(1)
                                .testo("testo")
                                .build()))
                        .build()));

        assertAll(() -> eventoService.getEventoById(1L));
    }

    @Test
    void modificaEventoThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventoService.modificaEvento(null, 0L));
    }

    @Test
    void modificaEventoThrowsEventoNonTrovato() {

        when(eventoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.modificaEvento(null, 1L));
    }

    @Test
    void modificaEventoThrowsDataInizioDopoDataFine() {

        CreaModificaEventoRequest request = new CreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(1),
                "1",
                "1",
                "nome luogo"
        );

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        assertThrows(BadRequestException.class,
                () -> eventoService.modificaEvento(request, 1L));
    }

    @Test
    void modificaEventoThrowsEventoNelPassato() {

        CreaModificaEventoRequest request = new CreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().plusHours(1),
                "1",
                "1",
                "nome luogo"
        );

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        assertThrows(BadRequestException.class,
                () -> eventoService.modificaEvento(request, 1L));
    }

    @Test
    void modificaEventoWithLuogoPresent() {

        CreaModificaEventoRequest request = new CreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "1",
                "nome luogo"
        );

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(luogoRepository.getLuogoByNome(any())).thenReturn(Optional.of(new Luogo()));

        assertAll(() -> eventoService.modificaEvento(request, 1L));
    }

    @Test
    void modificaEventoWithLuogoEmpty() {

        CreaModificaEventoRequest request = new CreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "1",
                "nome luogo"
        );

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(luogoRepository.getLuogoByNome(any())).thenReturn(Optional.empty());

        assertAll(() -> eventoService.modificaEvento(request, 1L));
    }

    @Test
    void adminGetAllEventiThrowsEventiNonTrovati() {

        when(eventoRepository.findAll()).thenReturn(List.of());

        assertThrows(NotFoundException.class,
                () -> eventoService.adminGetAllEventi());
    }

    @Test
    void adminGetAllEventiSuccessful() {

        User organizzatore = new UserBuilder()
                .build();

        when(eventoRepository.findAll())
                .thenReturn(List.of(new EventoBuilder()
                        .eventoId(1L)
                        .titolo("titolo")
                        .descrizione("descrizione")
                        .dataInizio(LocalDateTime.now().plusHours(1))
                        .dataFine(LocalDateTime.now().plusHours(2))
                        .dataCreazione(LocalDateTime.now())
                        .luogo(new LuogoBuilder()
                                .lat("1")
                                .lng("1")
                                .nome("nome luogo")
                                .build())
                        .organizzatore(organizzatore)
                        .iscritti(List.of(new UserBuilder()
                                .username("username")
                                .build()))
                        .recensioni(List.of(new RecensioneBuilder()
                                .user(new UserBuilder().username("username").build())
                                .voto(1)
                                .testo("testo")
                                .build()))
                        .build()));

        assertAll(() -> eventoService.adminGetAllEventi());
    }

    @Test
    void adminCreaEventoThrowsDataInizioUgualeADataFine() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "1",
                "2",
                "nome luogo",
                "username organizzatore"
        );

        assertThrows(BadRequestException.class,
                () -> eventoService.adminCreaEvento(request));
    }

    @Test
    void adminCreaEventoThrowsEventoNelPassato() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "1",
                "2",
                "nome luogo",
                "username organizzatore"
        );

        assertThrows(BadRequestException.class,
                () -> eventoService.adminCreaEvento(request));
    }

    @Test
    void adminCreaEventoThrowsCompilareTuttiICampi() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo",
                ""
        );

        assertThrows(BadRequestException.class,
                () -> eventoService.adminCreaEvento(request));
    }

    @Test
    void adminCreaEventoThrowsNonEsisteOrganizzatore() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo",
                "username organizzatore"
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> eventoService.adminCreaEvento(request));
    }

    @Test
    void adminCreaEventoThrowsPermessiNonAdatti() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo",
                "username organizzatore"
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        assertThrows(ForbiddenException.class,
                () -> eventoService.adminCreaEvento(request));
    }

    @Test
    void adminCreaEventoSuccessful() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo",
                "username organizzatore"
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertAll(() -> eventoService.adminCreaEvento(request));
    }

    @Test
    void adminModificaEventoThrowsIdNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventoService.adminModificaEvento(null, 0L));
    }

    @Test
    void adminModificaEventoThrowsEventoNonTrovato() {

        when(eventoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.adminModificaEvento(null, 1L));
    }

    @Test
    void adminModificaEventoThrowsDataInizioDopoDataFine() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now(),
                "1",
                "2",
                "nome luogo",
                "username organizzatore");

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        assertThrows(BadRequestException.class,
                () -> eventoService.adminModificaEvento(request, 1L));
    }

    @Test
    void adminModificaEventoThrowsDataInizioNelPassato() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().minusHours(1),
                "1",
                "2",
                "nome luogo",
                "username organizzatore");

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        assertThrows(BadRequestException.class,
                () -> eventoService.adminModificaEvento(request, 1L));
    }

    @Test
    void adminModificaEventoThrowsOrganizzatoreNonEsistente() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo",
                "username organizzatore");

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> eventoService.adminModificaEvento(request, 1L));
    }

    @Test
    void adminModificaEventoThrowsPermessiNonAdatti() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo",
                "username organizzatore");

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(luogoRepository.getLuogoByNome(any())).thenReturn(Optional.of(new Luogo()));

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        assertThrows(ForbiddenException.class,
                () -> eventoService.adminModificaEvento(request, 1L));
    }

    @Test
    void adminModificaEventoSuccessful() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo",
                "username organizzatore");

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(luogoRepository.getLuogoByNome(any())).thenReturn(Optional.of(new Luogo()));

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertAll(() -> eventoService.adminModificaEvento(request, 1L));
    }

    @Test
    void iscrizioneEventoThrowsIdNonValido() {

        IscrizioneEventoRequest request =
                new IscrizioneEventoRequest(0L, 1L);

        assertThrows(BadRequestException.class,
                () -> eventoService.iscrizioneEvento(request));
    }

    @Test
    void iscrizioneEventoThrowsEventoNonTrovato() {

        IscrizioneEventoRequest request =
                new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.iscrizioneEvento(request));
    }

    @Test
    void iscrizioneEventoThrowsTuristaNonTrovato() {

        IscrizioneEventoRequest request =
                new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.iscrizioneEvento(request));
    }

    @Test
    void iscrizioneEventoThrowsPermessiNonAdatti() {

        IscrizioneEventoRequest request =
                new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(ForbiddenException.class,
                () -> eventoService.iscrizioneEvento(request));
    }

    @Test
    void iscrizioneEventoWithNoIscritti() {

        IscrizioneEventoRequest request =
                new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new EventoBuilder()
                .iscritti(List.of())
                .build()));

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        assertAll(() -> eventoService.iscrizioneEvento(request));
    }

    @Test
    void iscrizioneEventoThrowsTuristaGiaIscritto() {

        IscrizioneEventoRequest request =
                new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new EventoBuilder()
                .iscritti(List.of(new UserBuilder()
                        .userId(1L)
                        .build()))
                .build()));

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.TURISTA)
                .build()));

        assertThrows(BadRequestException.class,
                () -> eventoService.iscrizioneEvento(request));
    }

    @Test
    void iscrizioneEventoWithTuristiIscritti() {

        List<User> iscritti = new ArrayList<>();

        User iscritto = new UserBuilder()
                .userId(2L)
                .build();

        iscritti.add(iscritto);

        Evento evento = new EventoBuilder()
                .iscritti(iscritti)
                .build();

        User turista = new UserBuilder()
                .userId(1L)
                .ruolo(Ruolo.TURISTA)
                .build();

        IscrizioneEventoRequest request =
                new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.of(evento));

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(turista));

        assertAll(() -> eventoService.iscrizioneEvento(request));
    }

    @Test
    void annullaIscrizioneThrowsIdNonValido() {

        IscrizioneEventoRequest request = new IscrizioneEventoRequest(0L, 1L);

        assertThrows(BadRequestException.class,
                () -> eventoService.annullaIscrizione(request));
    }

    @Test
    void annullaIscrizioneThrowsEventoNonTrovato() {

        IscrizioneEventoRequest request = new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.annullaIscrizione(request));
    }

    @Test
    void annullaIscrizioneThrowsTuristaNonTrovato() {

        IscrizioneEventoRequest request = new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.annullaIscrizione(request));
    }

    @Test
    void annullaIscrizioneThrowsPermessiNonAdatti() {

        IscrizioneEventoRequest request = new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new Evento()));

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(ForbiddenException.class,
                () -> eventoService.annullaIscrizione(request));
    }

    @Test
    void annullaIscrizioneSuccessful() {

        List<User> iscritti = new ArrayList<>();

        IscrizioneEventoRequest request = new IscrizioneEventoRequest(1L, 1L);

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new EventoBuilder()
                .iscritti(iscritti)
                .build()));

        when(userRepository.findByUserId(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        assertAll(() -> eventoService.annullaIscrizione(request));
    }

    @Test
    void getEventiByTuristaThrowsUsernameNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventoService.getEventiByTurista(""));
    }

    @Test
    void getEventiByTuristaThrowsTuristaNonEsistente() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(NotFoundException.class,
                () -> eventoService.getEventiByTurista("fonti"));
    }

    @Test
    void getEventiByTuristaSuccessful() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        when(eventoRepository.findAllByIscrittiIsContaining(any()))
                .thenReturn(List.of(new EventoBuilder()
                        .iscritti(List.of(new UserBuilder()
                            .username("username")
                            .build()))
                        .recensioni(List.of(new RecensioneBuilder()
                        .user(new UserBuilder().username("a").build())
                        .voto(2)
                        .testo("testo")
                        .build()))
                        .eventoId(1L)
                        .titolo("titolo")
                        .descrizione("descrizione")
                        .dataInizio(LocalDateTime.now().plusHours(1))
                        .dataFine(LocalDateTime.now().plusHours(2))
                        .dataCreazione(LocalDateTime.now())
                        .luogo(new LuogoBuilder()
                                .lat("1")
                                .lng("2")
                                .nome("nome luogo")
                                .build())
                        .organizzatore(new UserBuilder().username("aa").build())
                        .build()));

        assertAll(() -> eventoService.getEventiByTurista("fonti"));
    }

    @Test
    void rimuoviTuristaDaEventoThrowsUsernameNonValido() {

        assertThrows(BadRequestException.class,
                () -> eventoService.rimuoviTuristaDaEvento("", 1L));
    }

    @Test
    void rimuoviTuristaDaEventoThrowsTuristaNonEsistente() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        assertThrows(NotFoundException.class,
                () -> eventoService.rimuoviTuristaDaEvento("fonti", 1L));
    }

    @Test
    void rimuoviTuristaDaEventoThrowsIdNonValido() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        assertThrows(BadRequestException.class,
                () -> eventoService.rimuoviTuristaDaEvento("fonti", 0L));
    }

    @Test
    void rimuoviTuristaDaEventoThrowsEventoNonTrovato() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        when(eventoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> eventoService.rimuoviTuristaDaEvento("fonti", 1L));
    }

    @Test
    void rimuoviTuristaDaEventoThrowsTuristaNonIscritto() {

        List<User> iscritti = new ArrayList<>();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build()));

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new EventoBuilder()
                .iscritti(iscritti)
                .build()));

        assertThrows(BadRequestException.class,
                () -> eventoService.rimuoviTuristaDaEvento("fonti", 1L));
    }

    @Test
    void rimuoviTuristaDaEventoSuccessful() {

        List<User> iscritti = new ArrayList<>();

        User turista = new UserBuilder()
                .ruolo(Ruolo.TURISTA)
                .build();

        iscritti.add(turista);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(turista));

        when(eventoRepository.findById(any())).thenReturn(Optional.of(new EventoBuilder()
                .iscritti(iscritti)
                .build()));

        assertAll(() -> eventoService.rimuoviTuristaDaEvento("fonti", 1L));
    }

    @Test
    void getEventoByIdSuccessfulWithEventoPassato() {

        User organizzatore = new UserBuilder()
                .build();

        when(eventoRepository.findById(any()))
                .thenReturn(Optional.of(new EventoBuilder()
                        .eventoId(1L)
                        .titolo("titolo")
                        .descrizione("descrizione")
                        .dataInizio(LocalDateTime.now().minusHours(2))
                        .dataFine(LocalDateTime.now().minusHours(1))
                        .dataCreazione(LocalDateTime.now())
                        .luogo(new LuogoBuilder()
                                .lat("1")
                                .lng("1")
                                .nome("nome luogo")
                                .build())
                        .organizzatore(organizzatore)
                        .iscritti(List.of(new UserBuilder()
                                .username("username")
                                .build()))
                        .recensioni(List.of(new RecensioneBuilder()
                                .user(new UserBuilder().username("username").build())
                                .voto(1)
                                .testo("testo")
                                .build()))
                        .build()));

        assertAll(() -> eventoService.getEventoById(1L));
    }

    @Test
    void getEventoByIdSuccessfulWithEventoPresente() {

        User organizzatore = new UserBuilder()
                .build();

        when(eventoRepository.findById(any()))
                .thenReturn(Optional.of(new EventoBuilder()
                        .eventoId(1L)
                        .titolo("titolo")
                        .descrizione("descrizione")
                        .dataInizio(LocalDateTime.now().minusHours(2))
                        .dataFine(LocalDateTime.now().plusHours(2))
                        .dataCreazione(LocalDateTime.now())
                        .luogo(new LuogoBuilder()
                                .lat("1")
                                .lng("1")
                                .nome("nome luogo")
                                .build())
                        .organizzatore(organizzatore)
                        .iscritti(List.of(new UserBuilder()
                                .username("username")
                                .build()))
                        .recensioni(List.of(new RecensioneBuilder()
                                .user(new UserBuilder().username("username").build())
                                .voto(1)
                                .testo("testo")
                                .build()))
                        .build()));

        assertAll(() -> eventoService.getEventoById(1L));
    }

    @Test
    void adminCreaEventoSuccessfulWithLuogo() {

        AdminCreaModificaEventoRequest request = new AdminCreaModificaEventoRequest(
                "titolo",
                "descrizione",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "1",
                "2",
                "nome luogo",
                "username organizzatore"
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserBuilder()
                .ruolo(Ruolo.ORGANIZZATORE)
                .build()));

        when(luogoRepository.getLuogoByNome(any())).thenReturn(Optional.of(new Luogo()));

        assertAll(() -> eventoService.adminCreaEvento(request));
    }
}