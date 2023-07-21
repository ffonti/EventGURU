package it.polimi.iswpf.repository;

import it.polimi.iswpf.model.Turista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TuristaRepository extends JpaRepository<Turista, Long> {

    Optional<Turista> findByUsername(String username);
}
