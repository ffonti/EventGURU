package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Luogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LuogoRepository extends JpaRepository<Luogo, Long> {

    //Prendo il luogo dato un nome
    Optional<Luogo> getLuogoByNome(String nome);
}
