package it.polimi.iswpf.service.implementation;

import it.polimi.iswpf.builder.EventoBuilder;
import it.polimi.iswpf.builder.LuogoBuilder;
import it.polimi.iswpf.builder.RecensioneBuilder;
import it.polimi.iswpf.builder.UserBuilder;
import it.polimi.iswpf.dto.request.AdminCreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.CreaModificaEventoRequest;
import it.polimi.iswpf.exception.BadRequestException;
import it.polimi.iswpf.exception.ForbiddenException;
import it.polimi.iswpf.exception.InternalServerErrorException;
import it.polimi.iswpf.exception.NotFoundException;
import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.Luogo;
import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.observer.publisher.EventManager;
import it.polimi.iswpf.repository.EventoRepository;
import it.polimi.iswpf.repository.LuogoRepository;
import it.polimi.iswpf.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventoServiceImplTest {

    @Mock
    EventoRepository eventoRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    LuogoRepository luogoRepository;

    @Mock
    EventManager eventManager = EventManager.getInstance();

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

        //TODO entrare dentro l'observer

        assertAll(() -> eventoService.creaEvento(new CreaModificaEventoRequest(
                        "titolo",
                        "descrizione",
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(2),
                        "1",
                        "1",
                        "nome luogo"
                ), 1L));
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
    void adminModificaEvento() {
    }

    @Test
    void iscrizioneEvento() {
    }

    @Test
    void annullaIscrizione() {
    }

    @Test
    void getEventiByTurista() {
    }

    @Test
    void rimuoviTuristaDaEvento() {
    }
}