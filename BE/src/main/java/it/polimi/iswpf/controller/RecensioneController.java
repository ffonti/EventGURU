package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.InviaRecensioneRequest;
import it.polimi.iswpf.dto.response.MessageResponse;
import it.polimi.iswpf.dto.response.RecensioneDettagliataResponse;
import it.polimi.iswpf.dto.response.RecensioneResponse;
import it.polimi.iswpf.service._interface.RecensioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per le recensioni.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recensione")
public class RecensioneController {

    private final RecensioneService recensioneService;

    /**
     * Metodo per salvare una recensione di un turista. Chiama il service che farà
     * opportuni controlli prima di salvare la recensione sul database.
     * @param request DTO con i dati della recensione -> {@link InviaRecensioneRequest}.
     * @param eventoId Id dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id del turista, passato in modo dinamico tramite l'endpoint.
     * @return Messaggio di risposta al client.
     */
    @PostMapping("/inviaRecensione/{eventoId}/{turistaId}")
    public ResponseEntity<MessageResponse> inviaRecensione(
            @RequestBody InviaRecensioneRequest request,
            @PathVariable String eventoId,
            @PathVariable String turistaId) {

        recensioneService.inviaRecensione(request, Long.parseLong(eventoId), Long.parseLong(turistaId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Recensione inviata con successo"));
    }

    /**
     * Metodo per prendere tutte le recensioni di un dato evento.
     * @param eventoId Id univoco dell'evento, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati delle recensioni -> {@link RecensioneResponse}.
     */
    @GetMapping("getByEvento/{eventoId}")
    public ResponseEntity<List<RecensioneResponse>> getByEvento(@PathVariable String eventoId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(recensioneService.getByEvento(Long.parseLong(eventoId)));
    }

    /**
     * Metodo per prendere tutti i dati di una singola recensione.
     * @param eventoId Id univoco dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param usernameTurista Username univoco del turista che ha recensito l'evento,
     * passato in modo dinamico tramite l'endpoint.
     * @return DTO con i dati della recensione -> {@link RecensioneDettagliataResponse}.
     */
    @GetMapping("get/{eventoId}/{usernameTurista}")
    public ResponseEntity<RecensioneDettagliataResponse> getRecensione(
            @PathVariable String eventoId,
            @PathVariable String usernameTurista) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(recensioneService.getRecensione(Long.parseLong(eventoId), usernameTurista));
    }
}
