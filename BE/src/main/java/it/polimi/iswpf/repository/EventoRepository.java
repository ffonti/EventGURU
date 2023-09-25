package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Evento;
import it.polimi.iswpf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia responsabile della comunicazione col database per il model {@link Evento}.
 * Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface EventoRepository extends JpaRepository<Evento, Long> {

    /**
     * Si cercano gli eventi organizzati da un dato organizzatore.
     * @param organizzatore Organizzatore di cui fetchare gli eventi.
     * @return Una possibile lista di eventi organizzati dall'user stesso.
     */
    Optional<List<Evento>> findAllByOrganizzatore(User organizzatore);

    /**
     * Si cercano gli eventi in cui un dato turista è iscritto.
     * @param turista Turista di cui fetchare gli eventi a cui è iscritto.
     * @return Lista di Eventi a cui è iscritto il turista.
     */
    List<Evento> findAllByIscrittiIsContaining(User turista);

    /**
     * Si cercano gli eventi organizzato da un dato organizzatore.
     * @param organizzatoreId Id univoco dell'organizzatore.
     * @return Lista di Eventi organizzati dall'user stesso.
     */
    List<Evento> findAllByOrganizzatoreUserId(Long organizzatoreId);
}
