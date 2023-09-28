package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.InviaRecensioneRequest;
import it.polimi.iswpf.dto.response.RecensioneDettagliataResponse;
import it.polimi.iswpf.dto.response.RecensioneResponse;
import it.polimi.iswpf.service.implementation.RecensioneServiceImpl;

import java.util.List;

/**
 * Interfaccia che contiene le firme dei metodi del service che si occupa delle recensioni.
 * Implementazione -> {@link RecensioneServiceImpl}.
 */
public interface RecensioneService {

    /**
     * Metodo per salvare sul database una recensione di un utente.
     * @param request DTO con i dati della recensione.
     * @param eventoId Id dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id del turista, passato in modo dinamico tramite l'endpoint.
     */
    void inviaRecensione(InviaRecensioneRequest request, Long eventoId, Long turistaId);

    /**
     * Metodo per prendere tutte le recensioni di un dato evento.
     * @param eventoId Id univoco dell'evento, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati delle recensioni.
     */
    List<RecensioneResponse> getByEvento(Long eventoId);

    /**
     * Metodo per prendere tutti i dati di una singola recensione.
     * @param eventoId Id univoco dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param usernameTurista Username univoco del turista che ha recensito l'evento,
     * passato in modo dinamico tramite l'endpoint.
     * @return DTO con i dati della recensione.
     */
    RecensioneDettagliataResponse getRecensione(Long eventoId, String usernameTurista);
}
