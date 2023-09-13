package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.InviaRecensioneRequest;
import it.polimi.iswpf.service.implementation.RecensioneServiceImpl;

/**
 * Interfaccia che contiene le firme dei metodi del service.
 * Implementazione -> {@link RecensioneServiceImpl}.
 */
public interface RecensioneService {

    /**
     * Metodo per salvare sul database una recensione di un utente.
     * @param request DTO con i dati della recensione -> {@link InviaRecensioneRequest}.
     * @param eventoId Id dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id del turista, passato in modo dinamico tramite l'endpoint.
     */
    void inviaRecensione(InviaRecensioneRequest request, Long eventoId, Long turistaId);
}
