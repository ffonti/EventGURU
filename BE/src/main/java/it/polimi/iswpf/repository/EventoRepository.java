package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @- Interfaccia responsabile della comunicazione col database.
 * @- Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface EventoRepository extends JpaRepository<Evento, Long> {
}
