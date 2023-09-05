package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.AdminCreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.CreaModificaEventoRequest;
import it.polimi.iswpf.dto.response.*;
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
     * @param request DTO con i dati per la creazione dell'evento -> {@link CreaModificaEventoRequest}
     * @return Messaggio di avvenuta creazione.
     */
    @PostMapping("/crea")
    public ResponseEntity<CreaModificaEventoResponse> creaEvento(@RequestBody CreaModificaEventoRequest request) {

        eventoService.creaEvento(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreaModificaEventoResponse("Evento creato con successo!"));
    }

    /**
     * Metodo per prendere tutti gli eventi dato un organizzatore.
     * @param organizzatoreId Id dell'organizzatore da cui prendere gli eventi, passato in modo dinamico con l'endpoint.
     * @return Lista di DTO con gli eventi creati dall'organizzatore -> {@link GetEventoResponse}.
     */
    @GetMapping("/getByOrganizzatore/{organizzatoreId}")
    public ResponseEntity<List<GetEventoResponse>> getEventiByOrganizzatore(@PathVariable String organizzatoreId) {

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
    public ResponseEntity<EliminaEventoResponse> creaEvento(@PathVariable String eventoId) {

        eventoService.eliminaEvento(Long.parseLong(eventoId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new EliminaEventoResponse("Evento eliminato con successo!"));
    }


    /**
     * Metodo per prendere un evento dato l'id.
     * @param eventoId Id dell'evento, passato in modo dinamico con l'endpoint.
     * @return DTO con l'evento richiesto -> {@link GetEventoResponse}.
     */
    @GetMapping("/getEventoById/{eventoId}")
    public ResponseEntity<GetEventoResponse> getEventoById(@PathVariable String eventoId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoService.getEventoById(Long.parseLong(eventoId)));
    }

    /**
     * Metodo per modificare i dati di un evento già esistente. Chiama il service che controllerà
     * la validità dei dati prima di procedere con l'effettiva modifica.
     * @param request DTO con nuovi dati per aggiornare l'evento -> {@link CreaModificaEventoRequest}.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di avvenuta modifica.
     */
    @PutMapping("/modifica/{eventoId}")
    public ResponseEntity<CreaModificaEventoResponse> modificaEvento(
            @RequestBody CreaModificaEventoRequest request,
            @PathVariable String eventoId) {

        eventoService.modificaEvento(request, Long.parseLong(eventoId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CreaModificaEventoResponse("Evento modificato con successo"));
    }

    /**
     * Metodo per prendere tutti gli eventi presenti sul database.
     * @return Lista di DTO con tutti i dati di ogni evento -> {@link AllEventiResponse}.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<AllEventiResponse>> getAllEventi() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoService.adminGetAllEventi());
    }

    /**
     * Metodo per gli admin. Permette di creare un evento scegliendo l'organizzatore.
     * Salva l'utente sul db dopo aver effettuato tutti i controlli di validità dei dati.
     * @param request DTO con i dati dell'evento da creare -> {@link AdminCreaModificaEventoRequest}.
     * @return Messaggio di avvenuta creazione.
     */
    @PostMapping("/adminCrea")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreaModificaEventoResponse> adminCreaEvento(
            @RequestBody AdminCreaModificaEventoRequest request) {

        eventoService.adminCreaEvento(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CreaModificaEventoResponse("Evento creato con successo"));
    }

    /**
     * Metodo per gli admin. Permette di modificare i dati di un evento già esistente.
     * Chiama il service che controllerà la validità dei dati prima di procedere
     * con l'effettiva modifica.
     * @param request DTO con nuovi dati per aggiornare l'evento -> {@link AdminCreaModificaEventoRequest}.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di avvenuta modifica.
     */
    @PutMapping("/adminModifica/{eventoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreaModificaEventoResponse> adminModificaEvento(
            @RequestBody AdminCreaModificaEventoRequest request,
            @PathVariable String eventoId) {

        eventoService.adminModificaEvento(request, Long.parseLong(eventoId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CreaModificaEventoResponse("Evento modificato con successo"));
    }

    @GetMapping("iscrizione/{eventoId}/{turistaId}")
    public ResponseEntity<IscrizioneEventoResponse> iscrizioneEvento(
            @PathVariable String eventoId,
            @PathVariable String turistaId) {

        eventoService.iscrizioneEvento(Long.parseLong(eventoId), Long.parseLong(turistaId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new IscrizioneEventoResponse("Iscrizione avvenuta con successo"));
    }

    /**
     * Metodo per annullare l'iscrizione di un turista a un evento.
     * @param eventoId Id dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id del turista, passato in modo dinamico tramite l'endpoint.
     */
    @GetMapping("annullaIscrizione/{eventoId}/{turistaId}")
    public ResponseEntity<AnnullaIscrizioneResponse> annullaIscrizione(
            @PathVariable String eventoId,
            @PathVariable String turistaId) {

        eventoService.annullaIscrizione(Long.parseLong(eventoId), Long.parseLong(turistaId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AnnullaIscrizioneResponse("Iscrizione annullata con successo"));
    }
}
