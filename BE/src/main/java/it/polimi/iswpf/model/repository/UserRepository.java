package it.polimi.iswpf.model.repository;

import it.polimi.iswpf.model._enum.Ruolo;
import it.polimi.iswpf.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

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

    /**
     * Si cercano i followers di un dato organizzatore
     * @param organizzatore Organizzatore di cui prendere i followers.
     * @param ruolo Ruolo degli utenti per filtrarli.
     * @return Lista con i followers dell'organizzatore.
     */
    List<User> findAllBySeguitiAndRuolo(User organizzatore, Ruolo ruolo);

    /**
     * Si cercano tutti gli utenti iscritti alla newsletter.
     * @param ruolo Ruolo degli utenti per filtrarli.
     * @return Lista con gli utenti iscritti alla newsletter.
     */
    List<User> findAllByIscrittoNewsletterIsTrueAndRuolo(Ruolo ruolo);
}
