package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.CreaEventoRequest;
import it.polimi.iswpf.dto.response.CreaEventoResponse;
import it.polimi.iswpf.dto.response.GetAllEventiByOrganizzatoreResponse;
import it.polimi.iswpf.service._interface.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param request DTO con i dati per la creazione dell'evento -> {@link CreaEventoRequest}
     * @return Messaggio di avvenuta creazione.
     */
    @PostMapping("/crea")
    public ResponseEntity<CreaEventoResponse> creaEvento(@RequestBody CreaEventoRequest request) {

        eventoService.creaEvento(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreaEventoResponse("Evento creato con successo!"));
    }

    /**
     * Metodo per prendere tutti gli eventi dato un organizzatore.
     * @param organizzatoreId Id dell'organizzatore da cui prendere gli eventi, passato in modo dinamico con l'endpoint.
     * @return Lista di DTO con gli eventi creati dall'organizzatore {@link GetAllEventiByOrganizzatoreResponse}
     */
    @GetMapping("/getByOrganizzatore/{organizzatoreId}")
    public ResponseEntity<List<GetAllEventiByOrganizzatoreResponse>> getEventiByOrganizzatore(@PathVariable String organizzatoreId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventoService.getAllEventi(Long.parseLong(organizzatoreId)));
    }
}
