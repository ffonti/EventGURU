package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Recensione;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @- Interfaccia responsabile della comunicazione col database.
 * @- Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface RecensioneRepository extends JpaRepository<Recensione, Long> {
}
