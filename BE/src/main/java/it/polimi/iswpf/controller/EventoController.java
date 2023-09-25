package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.AdminCreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.CreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.IscrizioneEventoRequest;
import it.polimi.iswpf.dto.response.EventoResponse;
import it.polimi.iswpf.dto.response.MessageResponse;
import it.polimi.iswpf.service._interface.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per gli eventi.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/evento")
public class EventoController {

    private final EventoService eventoService;

    /**
     * Metodo per creare un evento. Chiama il service che controllerà la validità
     * dei dati prima di salvarli sul database.
     * @param request DTO con i dati per la creazione dell'evento.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'ednpoint.
     * @return Messaggio di avvenuta creazione.
     */
    @PostMapping("/crea/{organizzatoreId}")
    public ResponseEntity<MessageResponse> creaEvento(
            @RequestBody CreaModificaEventoRequest request,
            @PathVariable String organizzatoreId) {

        eventoService.creaEvento(request, Long.parseLong(organizzatoreId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("Evento creato con successo!"));
    }

    /**
     * Metodo per prendere tutti gli eventi dato un organizzatore.
     * @param organizzatoreId Id dell'organizzatore da cui prendere gli eventi, passato in modo dinamico con l'endpoint.
     * @return Lista di DTO con gli eventi creati dall'organizzatore.
     */
    @GetMapping("/getByOrganizzatore/{organizzatoreId}")
    public ResponseEntity<List<EventoResponse>> getEventiByOrganizzatore(
            @PathVariable String organizzatoreId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoService.getAllEventi(Long.parseLong(organizzatoreId)));
    }

    /**
     * Metodo per eliminare un evento. Chiama il service che eliminerà l'evento dal database.
     * @param eventoId Id dell'evento da eliminare, passato in modo dinamico con l'endpoint.
     * @return Messaggio di avvenuta eliminazione.
     */
    @DeleteMapping("/eliminaEvento/{eventoId}")
    public ResponseEntity<MessageResponse> eliminaEvento(@PathVariable String eventoId) {

        eventoService.eliminaEvento(Long.parseLong(eventoId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Evento eliminato con successo!"));
    }

    /**
     * Metodo per prendere un evento dato l'id.
     * @param eventoId Id dell'evento, passato in modo dinamico con l'endpoint.
     * @return DTO con l'evento richiesto.
     */
    @GetMapping("/getEventoById/{eventoId}")
    public ResponseEntity<EventoResponse> getEventoById(@PathVariable String eventoId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoService.getEventoById(Long.parseLong(eventoId)));
    }

    /**
     * Metodo per modificare i dati di un evento già esistente. Chiama il service che controllerà
     * la validità dei dati prima di procedere con l'effettiva modifica.
     * @param request DTO con nuovi dati per aggiornare l'evento.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di avvenuta modifica.
     */
    @PutMapping("/modifica/{eventoId}")
    public ResponseEntity<MessageResponse> modificaEvento(
            @RequestBody CreaModificaEventoRequest request,
            @PathVariable String eventoId) {

        eventoService.modificaEvento(request, Long.parseLong(eventoId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Evento modificato con successo"));
    }

    /**
     * Metodo per iscriversi a un evento.
     * @param request DTO con gli id univoci dell'evento e del turista.
     * @return Messaggio di avvenuta iscrizione.
     */
    @PostMapping("/iscrizione")
    public ResponseEntity<MessageResponse> iscrizioneEvento(@RequestBody IscrizioneEventoRequest request) {

        eventoService.iscrizioneEvento(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Iscrizione avvenuta con successo"));
    }

    /**
     * Metodo per prendere tutti gli eventi presenti sul database.
     * @return Lista di DTO con i dati di ogni evento.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<EventoResponse>> getAllEventi() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoService.adminGetAllEventi());
    }

    /**
     * Metodo per gli admin. Permette di creare un evento scegliendo l'organizzatore.
     * Salva l'utente sul db dopo aver effettuato tutti i controlli di validità dei dati.
     * @param request DTO con i dati dell'evento da creare.
     * @return Messaggio di avvenuta creazione.
     */
    @PostMapping("/adminCrea")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> adminCreaEvento(
            @RequestBody AdminCreaModificaEventoRequest request) {

        eventoService.adminCreaEvento(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Evento creato con successo"));
    }

    /**
     * Metodo per gli admin. Permette di modificare i dati di un evento già esistente.
     * Chiama il service che controllerà la validità dei dati prima di procedere
     * con l'effettiva modifica.
     * @param request DTO con nuovi dati per aggiornare l'evento.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di avvenuta modifica.
     */
    @PutMapping("/adminModifica/{eventoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> adminModificaEvento(
            @RequestBody AdminCreaModificaEventoRequest request,
            @PathVariable String eventoId) {

        eventoService.adminModificaEvento(request, Long.parseLong(eventoId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Evento modificato con successo"));
    }

    /**
     * Metodo per annullare l'iscrizione di un turista a un evento.
     * @param request DTO con id dell'evento e del turista.
     * @return Messaggio di avvenuta cancellazione.
     */
    @PostMapping("/annullaIscrizione")
    public ResponseEntity<MessageResponse> annullaIscrizione(@RequestBody IscrizioneEventoRequest request) {

        eventoService.annullaIscrizione(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Iscrizione annullata con successo"));
    }

    /**
     * Metodo per prendere tutti gli eventi a cui è iscritto un turista.
     * @param usernameTurista Username del turista, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con tutti i dati di ogni evento.
     */
    @GetMapping("getByTurista/{usernameTurista}")
    public ResponseEntity<List<EventoResponse>> getEventiByTurista(@PathVariable String usernameTurista) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoService.getEventiByTurista(usernameTurista));
    }

    /**
     * Metodo che permette a un organizzatore di rimuovere un turista iscritto a un dato evento.
     * @param usernameTurista Username univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @param eventoId Id univoco dell'evento, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di avvenuta rimozione.
     */
    @DeleteMapping("rimuoviTuristaDaEvento/{usernameTurista}/{eventoId}")
    public ResponseEntity<MessageResponse> rimuoviTuristaDaEvento(
            @PathVariable String usernameTurista,
            @PathVariable String eventoId) {

        eventoService.rimuoviTuristaDaEvento(usernameTurista, Long.parseLong(eventoId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Turista rimosso con successo"));
    }
}
