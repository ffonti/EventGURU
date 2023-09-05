package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.AdminCreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.CreaModificaEventoRequest;
import it.polimi.iswpf.dto.response.AllEventiResponse;
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
     * @param request DTO con i dati dell'evento da creare -> {@link CreaModificaEventoRequest}.
     */
    void creaEvento(CreaModificaEventoRequest request);

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

    /**
     * Metodo per modificare i dati di un evento già esistente.
     * @param request DTO con nuovi dati per aggiornare l'evento -> {@link CreaModificaEventoRequest}.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     */
    void modificaEvento(CreaModificaEventoRequest request, Long eventoId);

    /**
     * Metodo per prendere tutti gli eventi presenti sul database.
     * @return Lista di DTO con tutti i dati di ogni evento -> {@link AllEventiResponse}.
     */
    List<AllEventiResponse> adminGetAllEventi();

    /**
     * Metodo per gli admin. Permette di creare un evento scegliendo l'organizzatore.
     * Salva l'utente sul db dopo aver effettuato tutti i controlli di validità dei dati.
     * @param request DTO con i dati dell'evento da creare -> {@link AdminCreaModificaEventoRequest}.
     */
    void adminCreaEvento(AdminCreaModificaEventoRequest request);

    /**
     * Metodo per gli admin, usato per modificare i dati di un evento già esistente.
     * @param request DTO con nuovi dati per aggiornare l'evento -> {@link AdminCreaModificaEventoRequest}.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     */
    void adminModificaEvento(AdminCreaModificaEventoRequest request, Long eventoId);

    /**
     * Metodo per iscrivere un turista a un evento.
     * @param eventoId Id dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id del turista, passato in modo dinamico tramite l'endpoint.
     */
    void iscrizioneEvento(Long eventoId, Long turistaId);

    /**
     * Metodo per annullare l'iscrizione di un turista a un evento.
     * @param eventoId Id dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id del turista, passato in modo dinamico tramite l'endpoint.
     */
    void annullaIscrizione(Long eventoId, Long turistaId);
}
