package it.polimi.iswpf.controller;

import it.polimi.iswpf.dto.request.InviaRecensioneRequest;
import it.polimi.iswpf.dto.response.InviaRecensioneResponse;
import it.polimi.iswpf.service._interface.RecensioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<InviaRecensioneResponse> inviaRecensione(
            @RequestBody InviaRecensioneRequest request,
            @PathVariable String eventoId,
            @PathVariable String turistaId) {

        recensioneService.inviaRecensione(request, Long.parseLong(eventoId), Long.parseLong(turistaId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new InviaRecensioneResponse("Recensione inviata con successo"));
    }
}
