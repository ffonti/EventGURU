package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.dto.response.OrganizzatoreResponse;
import it.polimi.iswpf.dto.response.OrganizzatoriSeguitiResponse;
import it.polimi.iswpf.dto.response.UserResponse;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.service.implementation.UserServiceImpl;

import java.util.List;

/**
 * Interfaccia che contiene le firme dei metodi del service.
 * Implementazione -> {@link UserServiceImpl}.
 */
public interface UserService {

    /**
     * Metodo che, dato un id, prende l'utente dal database tramite la repository e lo ritorna al controller.
     * @param userId Id dell'utente.
     * @return Oggetto {@link User} con tutti i dati dell'utente.
     */
    UserResponse getUserData(Long userId);

    /**
     * Metodo che, dato un id e un DTO con i nuovi dati, modifica i dati dell'utente con quell'id.
     * @param userId Id dell'utente.
     * @param request DTO con i nuovi dati {@link UpdateUserDataRequest}.
     * @return L'oggetto utente con i dati aggiornati.
     */
    UserResponse updateUserData(Long userId, UpdateUserDataRequest request);

    /**
     * Metodo che permette all'admin di modificare i dati di un utente, dato un username.
     * @param username Username dell'utente da modificare.
     * @param request DTO con i nuovi dati {@link UpdateUserDataRequest}.
     * @return L'oggetto utente con i dati aggiornati.
     */
    UserResponse adminUpdateUserData(String username, UpdateUserDataRequest request);

    /**
     * Dato un ruolo ritorna una lista di utenti con quel ruolo.
     * @param ruolo Ruolo preso dall'endpoint.
     * @return Lista di utenti presa dal db.
     */
    List<UserResponse> getAll(String ruolo);

    /**
     * Elimina l'utente dal database dopo aver fatto diversi controlli.
     * @param userId Id dell'utente da eliminare.
     */
    void deleteAccount(Long userId);

    /**
     * Dato un username, ed eseguiti diversi controlli, ritorno l'utente con quell'username.
     * @param username Username dell'utente richiesto.
     * @return Istanza di {@link User} richiesta.
     */
    UserResponse getAdminUserData(String username);

    /**
     * Dato un username, viene eliminato l'account.
     * @param username Username dell'account da eliminare.
     */
    void adminDeleteAccount(String username);

    /**
     * Metodo per prendere tutti gli organizzatori presenti sul database.
     * @return Lista di DTO con i dati di ogni organizzatore -> {@link OrganizzatoreResponse}.
     */
    List<OrganizzatoreResponse> getAllOrganizzatori();

    /**
     * Metodo che permette a un turista di seguire un organizzatore, e quindi essere notificati alla creazione di un evento.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     */
    void seguiOrganizzatore(Long organizzatoreId, Long turistaId);

    /**
     * Metodo che, dato un turista, restituisce gli username degli organizzatori seguiti.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con gli username degli organizzatori -> {@link OrganizzatoriSeguitiResponse}.
     */
    List<OrganizzatoriSeguitiResponse> getOrganizzatoriSeguiti(Long turistaId);

    /**
     * Metodo che permette a un turista di smettere di seguire un organizzatore.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     */
    void smettiSeguireOrganizzatore(Long organizzatoreId, Long turistaId);
}
