package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Ruolo;
import it.polimi.iswpf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @- Interfaccia responsabile della comunicazione col database.
 * @- Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUserId(Long userId);

    Optional<List<User>> findAllByRuolo(Ruolo ruolo);
}
