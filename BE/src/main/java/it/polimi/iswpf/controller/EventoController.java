package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.CreaEventoRequest;
import it.polimi.iswpf.dto.response.CreaEventoResponse;
import it.polimi.iswpf.dto.response.GetAllEventiResponse;
import it.polimi.iswpf.model.Evento;
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

    @PostMapping("/crea")
    public ResponseEntity<CreaEventoResponse> creaEvento(@RequestBody CreaEventoRequest request) {

        eventoService.creaEvento(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreaEventoResponse("Evento creato con successo!"));
    }

    @GetMapping("/getAllEventi")
    public ResponseEntity<GetAllEventiResponse> getAllEventi() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GetAllEventiResponse(eventoService.getAllEventi()));
    }
}
