package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Luogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaccia responsabile della comunicazione col database per il model {@link Luogo}.
 * Estende l'interfaccia generica JpaRepository, a cui daremo il tipo della classe e il tipo dell'id.
 */
public interface LuogoRepository extends JpaRepository<Luogo, Long> {

    /**
     * Prendo tutti i dati di un luogo dato un nome.
     * @param nome Nome del luogo da fetchare.
     * @return Un possibile oggetto {@link Luogo} con tutti i dati richiesti.
     */
    Optional<Luogo> getLuogoByNome(String nome);
}
