package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Interfaccia responsabile della comunicazione col database per il model {@link User}.
 * Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Si cercano i dati di un utente dato il solo username.
     * @param username Username univoco dell'utente.
     * @return Un probabile utente con quell'username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Si cercano i dati di un utente dato il solo userId.
     * @param userId Id univoco dell'utente.
     * @return Un probabile utente con quell'id.
     */
    Optional<User> findByUserId(Long userId);

    /**
     * Si cercano tutti gli utenti aventi un dato ruolo.
     * @param ruolo Enum che caratterizza turisti, organizzatori e admin.
     * @return Una probabile lista di utenti con quel ruolo.
     */
    Optional<List<User>> findAllByRuolo(Ruolo ruolo);

    /**
     * Si cercano i dati di un utente data la sola email.
     * @param email Email dell'utente.
     * @return Un probabile utente con quella email.
     */
    Optional<User> findFirstByEmail(String email);
}
