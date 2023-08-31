package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.CreaEventoRequest;
import it.polimi.iswpf.dto.response.GetAllEventiByOrganizzatoreResponse;
import it.polimi.iswpf.service.implementation.EventoServiceImpl;

import java.util.List;

/**
 * Interfaccia che contiene le firme dei metodi del service.
 * Implementazione -> {@link EventoServiceImpl}.
 */
public interface EventoService {

    /**
     * Metodo per creare un evento. Salva l'utente sul db dopo aver effettuato tutti i controlli di validità dei dati.
     * @param request DTO con i dati dell'evento da creare -> {@link CreaEventoRequest}.
     */
    void creaEvento(CreaEventoRequest request);

    /**
     * Metodo per prendere tutti gli eventi di un organizzatore.
     * @param organizzatoreId Id dell'organizzatore di cui si vogliono prendere gli eventi.
     * @return Lista di DTO con i dati degli eventi {@link GetAllEventiByOrganizzatoreResponse}.
     */
    List<GetAllEventiByOrganizzatoreResponse> getAllEventi(Long organizzatoreId);

    /**
     * Metodo per eliminare un evento.
     * @param eventoId Id dell'evento da eliminare.
     */
    void eliminaEvento(Long eventoId);
}
