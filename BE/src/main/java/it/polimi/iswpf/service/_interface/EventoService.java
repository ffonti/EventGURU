package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.AdminCreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.CreaModificaEventoRequest;
import it.polimi.iswpf.dto.request.IscrizioneEventoRequest;
import it.polimi.iswpf.dto.response.EventoResponse;
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
    void creaEvento(CreaModificaEventoRequest request, Long organizzatoreId);

    /**
     * Metodo per prendere tutti gli eventi di un organizzatore.
     * @param organizzatoreId Id dell'organizzatore di cui si vogliono prendere gli eventi.
     * @return Lista di DTO con i dati degli eventi {@link EventoResponse}.
     */
    List<EventoResponse> getAllEventi(Long organizzatoreId);

    /**
     * Metodo per eliminare un evento.
     * @param eventoId Id dell'evento da eliminare.
     */
    void eliminaEvento(Long eventoId);

    /**
     * Metodo per prendere un evento dato un id.
     * @param eventoId Id del singolo evento.
     * @return DTO con i dati dell'evento richiesto -> {@link EventoResponse}.
     */
    EventoResponse getEventoById(Long eventoId);

    /**
     * Metodo per modificare i dati di un evento già esistente.
     * @param request DTO con nuovi dati per aggiornare l'evento -> {@link CreaModificaEventoRequest}.
     * @param eventoId Id dell'evento da modificare, passato in modo dinamico tramite l'endpoint.
     */
    void modificaEvento(CreaModificaEventoRequest request, Long eventoId);

    /**
     * Metodo per prendere tutti gli eventi presenti sul database.
     * @return Lista di DTO con tutti i dati di ogni evento -> {@link EventoResponse}.
     */
    List<EventoResponse> adminGetAllEventi();

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
     * @param request DTO con gli id univoci dell'evento e del turista.
     */
    void iscrizioneEvento(IscrizioneEventoRequest request);

    /**
     * Metodo per annullare l'iscrizione di un turista a un evento.
     * @param eventoId Id dell'evento, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id del turista, passato in modo dinamico tramite l'endpoint.
     */
    void annullaIscrizione(IscrizioneEventoRequest request);

    /**
     * Metodo per prendere tutti gli eventi a cui è iscritto un turista.
     * @param usernameTurista Username del turista, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con tutti i dati di ogni evento.
     */
    List<EventoResponse> getEventiByTurista(String usernameTurista);

    /**
     * Metodo che permette a un organizzatore di rimuovere un turista iscritto a un dato evento.
     * @param usernameTurista Username univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @param eventoId Id univoco dell'evento, passato in modo dinamico tramite l'endpoint.
     */
    void rimuoviTuristaDaEvento(String usernameTurista, Long eventoId);
}
