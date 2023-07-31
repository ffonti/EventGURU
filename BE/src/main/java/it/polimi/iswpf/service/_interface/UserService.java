package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.dto.request.UpdateUserDataRequest;
import it.polimi.iswpf.model.User;
import it.polimi.iswpf.service.implementation.UserServiceImpl;

import java.util.Optional;

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
    User getUserData(Long userId);

    /**
     * Metodo che, dato un id e un DTO con i nuovi dati, modifica i dati dell'utente con quell'id.
     * @param userId Id dell'utente.
     * @param request DTO con i nuovi dati {@link UpdateUserDataRequest}.
     * @return L'oggetto utente con i dati aggiornati.
     */
    User updateUserData(Long userId, UpdateUserDataRequest request);
}
