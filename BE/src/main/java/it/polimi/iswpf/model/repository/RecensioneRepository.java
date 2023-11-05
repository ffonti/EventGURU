package it.polimi.iswpf.model.repository;

import it.polimi.iswpf.model.entity.Recensione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaccia responsabile della comunicazione col database per il model {@link Recensione}.
 * Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    /**
     * Query per prendere una singola recensione dati i seguenti parametri.
     * @param evento_eventoId Id univoco dell'evento.
     * @param user_username Username univoco del turista.
     * @return Un oggetto che conterrà null oppure la recensione ricercata.
     */
    Optional<Recensione> getRecensioneByEvento_EventoIdAndUser_Username(Long evento_eventoId, String user_username);
}
