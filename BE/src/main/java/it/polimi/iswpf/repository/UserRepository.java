package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Interfaccia responsabile della comunicazione col database.
 * Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    //Si cercano i dati di un utente dato il solo username.
    Optional<User> findByUsername(String username);

    //Si cercano i dati di un utente dato il solo userId.
    Optional<User> findByUserId(Long userId);

    //Si cercano tutti gli utenti aventi un dato ruolo.
    Optional<List<User>> findAllByRuolo(Ruolo ruolo);

    Optional<User> findByEmail(String email);
}
