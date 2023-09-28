package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.dto.response.OrganizzatoreResponse;
import it.polimi.iswpf.dto.response.OrganizzatoreSeguitoResponse;
import it.polimi.iswpf.dto.response.UserResponse;
import it.polimi.iswpf.service.implementation.UserServiceImpl;

import java.util.List;

/**
 * Interfaccia che contiene le firme dei metodi del service inerenti all'user.
 * Implementazione -> {@link UserServiceImpl}.
 */
public interface UserService {

    /**
     * Metodo che, dato un id, prende l'utente dal database tramite la repository e lo ritorna al controller.
     * @param userId Id dell'utente.
     * @return DTO con tutti i dati dell'utente.
     */
    UserResponse getUserData(Long userId);

    /**
     * Metodo che, modifica i dati dell'utente con quell'id.
     * @param userId Id dell'utente.
     * @param request DTO con i nuovi dati.
     * @return DTO con i dati dell'utente aggiornati.
     */
    UserResponse updateUserData(Long userId, UpdateUserDataRequest request);

    /**
     * Metodo che permette all'admin di modificare i dati di un utente, dato un username.
     * @param username Username dell'utente da modificare.
     * @param request DTO con i nuovi dati.
     * @return DTO con i dati dell'utente aggiornati.
     */
    UserResponse adminUpdateUserData(String username, UpdateUserDataRequest request);

    /**
     * Elimina l'utente dal database dopo aver fatto diversi controlli.
     * @param userId Id dell'utente da eliminare.
     */
    void deleteAccount(Long userId);

    /**
     * Dato un username, ed eseguiti diversi controlli, ritorno l'utente con quell'username.
     * @param username Username dell'utente richiesto.
     * @return DTO con i dati dell'utente richiesto.
     */
    UserResponse getAdminUserData(String username);

    /**
     * Dato un username, viene eliminato l'account.
     * @param username Username dell'account da eliminare.
     */
    void adminDeleteAccount(String username);

    /**
     * Metodo per prendere tutti gli organizzatori presenti sul database.
     * @return Lista di DTO con i dati di ogni organizzatore.
     */
    List<OrganizzatoreResponse> getAllOrganizzatori();

    /**
     * Metodo per prendere tutti gli utenti di un dato ruolo.
     * @param ruolo Ruolo richiesto, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con i dati degli utenti richiesti.
     */
    List<UserResponse> getAllByRuolo(String ruolo);

    /**
     * Metodo che permette a un turista di seguire un organizzatore, e quindi essere notificati alla creazione di un evento.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     */
    void seguiOrganizzatore(Long organizzatoreId, Long turistaId);

    /**
     * Metodo che, dato un turista, restituisce gli username degli organizzatori seguiti.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     * @return Lista di DTO con gli username degli organizzatori.
     */
    List<OrganizzatoreSeguitoResponse> getOrganizzatoriSeguiti(Long turistaId);

    /**
     * Metodo che permette a un turista di smettere di seguire un organizzatore.
     * @param organizzatoreId Id univoco dell'organizzatore, passato in modo dinamico tramite l'endpoint.
     * @param turistaId Id univoco del turista, passato in modo dinamico tramite l'endpoint.
     */
    void smettiSeguireOrganizzatore(Long organizzatoreId, Long turistaId);
}
