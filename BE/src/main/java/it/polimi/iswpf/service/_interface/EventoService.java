package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.CreaEventoRequest;
import it.polimi.iswpf.dto.response.GetEventoResponse;
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
     * @return Lista di DTO con i dati degli eventi {@link GetEventoResponse}.
     */
    List<GetEventoResponse> getAllEventi(Long organizzatoreId);

    /**
     * Metodo per eliminare un evento.
     * @param eventoId Id dell'evento da eliminare.
     */
    void eliminaEvento(Long eventoId);

    /**
     * Metodo per prendere un evento dato un id.
     * @param eventoId Id del singolo evento.
     * @return DTO con i dati dell'evento richiesto -> {@link GetEventoResponse}.
     */
    GetEventoResponse getEventoById(Long eventoId);
}
