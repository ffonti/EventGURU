package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia responsabile della comunicazione col database.
 * Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface EventoRepository extends JpaRepository<Evento, Long> {

    //Si cercano gli eventi organizzati da un dato organizzatore.
    Optional<List<Evento>> findAllByOrganizzatore(User organizzatore);

    //Si cercano gli eventi in cui un dato turista è iscritto.
    List<Evento> findAllByIscrittiIsContaining(User turista);
}
