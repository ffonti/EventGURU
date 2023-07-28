package it.polimi.iswpf.service._interface;

import it.polimi.iswpf.model.User;
import it.polimi.iswpf.service.implementation.UserServiceImpl;

import java.util.Optional;

/**
 * Interfaccia che contiene le firme dei metodi del service.
 * Implementazione -> {@link UserServiceImpl}.
 */
public interface UserService {

    User getUserData(Long userId);
}
